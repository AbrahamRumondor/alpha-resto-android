package com.example.alfaresto_customersapp.ui.components.orderSummaryPage

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.data.network.NetworkUtils
import com.example.alfaresto_customersapp.data.remote.response.pushNotification.NotificationBody
import com.example.alfaresto_customersapp.data.remote.response.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.callbacks.FirestoreCallback
import com.example.alfaresto_customersapp.domain.error.Result.Error
import com.example.alfaresto_customersapp.domain.error.Result.Success
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.example.alfaresto_customersapp.domain.usecase.shipment.ShipmentUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity.Companion.ON_PROCESS
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_ADDRESS
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_PAYMENT_METHOD
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_TOKEN
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OrderSummaryViewModel @Inject constructor(
    @Named("db") private val db: FirebaseFirestore,
    private val menuUseCase: MenuUseCase,
    private val cartUseCase: CartUseCase,
    private val userUseCase: UserUseCase,
    private val orderUseCase: OrderUseCase,
    private val restaurantUseCase: RestaurantUseCase,
    private val fcmApiRepository: FcmApiRepository,
    private val shipmentUseCase: ShipmentUseCase
) : ViewModel() {

    private val _menus: MutableStateFlow<List<Menu>> = MutableStateFlow(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    private val _cart: MutableStateFlow<List<CartEntity>> = MutableStateFlow(emptyList())
    val carts: StateFlow<List<CartEntity>> = _cart

    private val _orders: MutableStateFlow<MutableList<Any?>> = MutableStateFlow(mutableListOf())
    val orders: StateFlow<List<Any?>> = _orders

    private val _restoID: MutableStateFlow<String> = MutableStateFlow("")
    val restoID: StateFlow<String> = _restoID

    private val _restoToken: MutableStateFlow<String> = MutableStateFlow("")
    val restoToken: StateFlow<String> = _restoToken

    private val _restoOpenHour: MutableStateFlow<String> = MutableStateFlow("")
    private val restoOpenHour: StateFlow<String> = _restoOpenHour

    private val _restoClosedHour: MutableStateFlow<String> = MutableStateFlow("")
    private val restoClosedHour: StateFlow<String> = _restoClosedHour

    private val _restoIsClosedTemporarily: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val restoIsClosedTemporarily: StateFlow<Boolean> = _restoIsClosedTemporarily

    private val _notAvailables: MutableStateFlow<Pair<List<CartEntity>, List<Menu>>> =
        MutableStateFlow(Pair(emptyList(), emptyList()))
    val notAvailables: StateFlow<Pair<List<CartEntity>, List<Menu>>> = _notAvailables

    private val cartNotAvailable = mutableListOf<CartEntity>()
    private val menuNotAvailable = mutableListOf<Menu>()

    init {
        fetchMenus()
        fetchCart()
        fetchResto()
        checkStock()
    }

    fun setPayment(method: String) {
        _orders.value[orders.value.size - 3] = method
        USER_PAYMENT_METHOD = method
    }

    fun setNotes(notes: String) {
        _orders.value[orders.value.size - 2] = notes
    }

    fun removeOrder(position: Int, menuId: String): Int {
        _orders.value.removeAt(position)
        viewModelScope.launch {
            cartUseCase.deleteMenu(menuId)
        }
        return _orders.value.size
    }

    fun makeOrders(
        address: Address?, orders: List<Menu>, total: Pair<Int, Int>
    ): MutableList<Any?> {
        val orderList = mutableListOf(
            address, total, "payment_method", "notes", "checkout"
        )

        orderList.addAll(1, orders)
        _orders.value = orderList

        return orderList
    }

    private fun fetchResto() {
        viewModelScope.launch {
            try {
                val restoID = restaurantUseCase.getRestaurantId()
                _restoID.value = restoID

                val restoToken = restaurantUseCase.getRestaurantToken()
                _restoToken.value = restoToken

                val restoOpenHour = restaurantUseCase.getRestaurantOpenHour()
                _restoOpenHour.value = restoOpenHour

                val restoClosedHour = restaurantUseCase.getRestaurantClosedHour()
                _restoClosedHour.value = restoClosedHour

                val isClosedTemporarily = restaurantUseCase.isRestaurantClosedTemporary()
                _restoIsClosedTemporarily.value = isClosedTemporarily
            } catch (e: Exception) {
                Timber.tag("RESTO").e("Error fetching resto: %s", e.message)
            }
        }
    }

    fun isRestoClosed(currentTime: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult((currentTime >= restoClosedHour.value && currentTime < restoOpenHour.value) || restoIsClosedTemporarily.value)
        }
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            try {
                menuUseCase.getMenus().collectLatest {
                    _menus.value = it
                }
            } catch (e: Exception) {
                Timber.tag("MENU").e("Error fetching menus: %s", e.message)
            }
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                cartUseCase.getCart().collect {
                    _cart.value = it
                }
            } catch (e: Exception) {
                Timber.tag("CART").e("Error fetching cart: %s", e.message)
            }
        }
    }

    private fun insertMenu(menuId: String, menuQty: Int) {
        viewModelScope.launch {
            val cartEntity = CartEntity(menuId = menuId, menuQty = menuQty)
            cartUseCase.insertMenu(cartEntity)
        }
    }

    fun addOrderQuantity(menuId: String, cart: CartEntity?, context: Context): Int {
        var isFromUserClick = menuId.isNotEmpty()

        var isValidAddition = 0
        menuUseCase.getMenuStock(menuId) { stock ->
            if (isFromUserClick) {
                isFromUserClick = false
                if (stock == 0) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.out_of_stock),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@getMenuStock
                }
                cart?.let {
                    Log.d("order", "ORSUM: stock: $stock && cartstock: ${cart.menuQty}")
                    if (it.menuQty < stock) {
                        viewModelScope.launch {
                            cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty + 1))
                            return@launch
                        }
                        isValidAddition++
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.reach_max_stock),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } ?: insertMenu(menuId = menuId, menuQty = 1)
                return@getMenuStock
            }
        }
        return isValidAddition
    }

    fun decreaseOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                if (cart.menuQty > 0) cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty - 1))
            }
        }
    }

    fun getOrderDocumentId(): String {
        return orderUseCase.getOrderDocID()
    }

    private fun getOrderItemDocumentId(orderId: String): String {
        return orderUseCase.getOrderItemDocID(orderId)
    }

    // TODO 1:userID,addressID,restoID (fetch dr firestore) | 2:menuID (fetch dari firestore)
    fun saveOrderInDatabase(onResult: (msg: Int?) -> Unit) {
        getUserFromDB(object : FirestoreCallback {
            override fun onSuccess(user: User?) {
                val NOTES = orders.value.size - 2
                val PAYMENT_METHOD = orders.value.size - 3
                val TOTAL = orders.value.size - 4

                val notes =
                    if (_orders.value[NOTES] != "notes") _orders.value[NOTES].toString() else ""

                val total = orders.value[TOTAL] as Pair<Int, Int>

                if (NetworkUtils.isConnectedToNetwork.value == false) {
                    onResult(R.string.no_internet)
                    return
                }

                if (!USER_PAYMENT_METHOD.isNullOrEmpty() && _orders.value.size > 4 && user != null && USER_ADDRESS != null) {
                    try {
                        val orderId = getOrderDocumentId()

                        db.runTransaction { transaction ->
                            USER_ADDRESS?.let { address ->
                                USER_TOKEN?.let { token ->
                                    val order = Order(
                                        id = orderId,
                                        userName = user.name,
                                        userId = user.id,
                                        fullAddress = address.address,
                                        restoID = restoID.value,
                                        date = Date(),
                                        paymentMethod = USER_PAYMENT_METHOD!!,
                                        totalPrice = total.second,
                                        latitude = address.latitude,
                                        longitude = address.longitude,
                                        userToken = token,
                                        restoToken = restoToken.value,
                                        notes = notes
                                    )
                                    val orderToFirebase = OrderResponse.toResponse(order)

                                    val menuRefs = _orders.value.subList(1, TOTAL).map { menu ->
                                        db.collection("menus").document((menu as Menu).id) to menu
                                    }

                                    val snapshots = menuRefs.map { (ref, _) ->
                                        transaction.get(ref)
                                    }

                                    // Validate stock availability
                                    snapshots.forEachIndexed { index, snapshot ->
                                        val menu = menuRefs[index].second
                                        val currentStock = snapshot.getLong("menu_stock")
                                            ?: throw Exception("Stock field missing")
                                        if (currentStock < (menu as Menu).orderCartQuantity) {
                                            throw Exception("Stock is below the threshold, transaction cancelled.")
                                        }
                                    }

                                    transaction.set(
                                        db.collection("orders").document(order.id), orderToFirebase
                                    )

                                    snapshots.forEachIndexed { index, snapshot ->
                                        val (ref, menu) = menuRefs[index]
                                        val currentStock = snapshot.getLong("menu_stock")
                                            ?: throw Exception("Stock field missing")
                                        val newStock = currentStock - (menu as Menu).orderCartQuantity

                                        val orderItem = OrderItem(
                                            id = getOrderItemDocumentId(order.id),
                                            menuName = menu.name,
                                            quantity = menu.orderCartQuantity,
                                            menuPrice = menu.price,
                                            menuImage = menu.image
                                        )
                                        val orderItemResponse = OrderItemResponse.toResponse(orderItem)

                                        transaction.set(
                                            db.collection("orders").document(order.id).collection("order_items")
                                                .document(orderItem.id), orderItemResponse
                                        )
                                        transaction.update(ref, "menu_stock", newStock)
                                    }
                                }
                            }
                        }.addOnSuccessListener {
                            viewModelScope.launch {
                                shipmentUseCase.createShipment(
                                    Shipment(
                                        orderID = orderId,
                                        statusDelivery = ON_PROCESS,
                                        userId = user.id
                                    )
                                )
                                cartUseCase.deleteAllMenus()
                            }

                            sendNotificationToResto()
                            Log.d("orsum", "success")
                            onResult(null)
                        }.addOnFailureListener { e ->
                            Timber.tag("test").d("transaction failed: %s", e.message)
                            onResult(R.string.failed_checkout_no_stock)
                        }
                    } catch (e: Exception) {
                        Timber.tag("test")
                            .d("transaction faled: %s", e.message)
                        onResult(R.string.failed_checkout_problems)
                    }
                } else {
                    onResult(R.string.failed_checkout_insufficient_field)
                }
            }

            override fun onFailure(exception: Exception) {
                onResult(R.string.failed_checkout_problems)
            }
        })
    }

    private fun getUserFromDB(callback: FirestoreCallback) {
        viewModelScope.launch {
            try {
                val user = userUseCase.getCurrentUser()
                callback.onSuccess(user.value)
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }
    }

    private fun sendNotificationToResto() {
        viewModelScope.launch {
            val newToken = restaurantUseCase.getRestaurantToken()
            _restoToken.value = newToken

            val messageDto = SendMessageDto(
                to = restoToken.value, notification = NotificationBody(
                    title = fcmOrderTitle,
                    body = fcmOrderBody,
                    link = fcmOrderLink
                )
            )

            when (val result = fcmApiRepository.sendMessage(messageDto)) {
                is Success -> {
                    Timber.tag("test").d("FCM SENT")
                }

                is Error -> {
                    Timber.tag("test").d("FCM FAILED")
                }

                else -> {
                    Timber.tag("test").d("FCM FAILED")
                }
            }
        }
    }

    fun getCurrentTime(): String {
        val date = Timestamp.now().toDate().toString()
        val time = date.substring(11, 16)
        return time
    }

    private fun checkStock() {
        viewModelScope.launch {
            carts.collectLatest { carts ->
                delay(500)
                for (i in carts.indices) {
                    menuUseCase.getMenuStock(carts[i].menuId) {
                        if (i == 0) {
                            cartNotAvailable.clear()
                            menuNotAvailable.clear()
                            _notAvailables.value = Pair(emptyList(), emptyList())
                        }

                        if (carts[i].menuQty > it) {
                            cartNotAvailable.add(carts[i])
                            menuNotAvailable.add(menus.value.first { menu -> menu.id == carts[i].menuId })
                        }

                        if (i == carts.size - 1) {
                            _notAvailables.value = Pair(cartNotAvailable, menuNotAvailable)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val fcmOrderTitle = "New Order"
        const val fcmOrderBody = "There's new order. Check your Resto App"
        const val fcmOrderLink = "alfaresto://order"
    }
}