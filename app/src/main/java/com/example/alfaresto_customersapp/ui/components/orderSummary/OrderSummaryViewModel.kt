package com.example.alfaresto_customersapp.ui.components.orderSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.data.remote.pushNotification.NotificationBody
import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.error.Result.Error
import com.example.alfaresto_customersapp.domain.error.Result.Success
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.network.NetworkUtils
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.example.alfaresto_customersapp.domain.usecase.shipment.ShipmentUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ADDRESS
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_TOKEN
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _restoClosedHour: MutableStateFlow<String> = MutableStateFlow("")
    val restoClosedHour: StateFlow<String> = _restoClosedHour

    init {
        fetchMenus()
        fetchCart()
        fetchResto()
    }

    fun setPayment(method: String) {
        _orders.value[orders.value.size - 2] = method
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
            address, total, "payment_method", "checkout"
        )

        orderList.addAll(1, orders)
        _orders.value = orderList

        return orderList
    }

    private fun fetchResto() {
        viewModelScope.launch {
            try {
                Timber.tag("closing").d("Fetching resto")
                val restoID = restaurantUseCase.getRestaurantId()
                _restoID.value = restoID

                val restoToken = restaurantUseCase.getRestaurantToken()
                _restoToken.value = restoToken

                val restoClosedHour = restaurantUseCase.getRestaurantClosedHour()
                _restoClosedHour.value = restoClosedHour
                Timber.tag("closing").d("Closing time: $restoClosedHour")
            } catch (e: Exception) {
                Timber.tag("RESTO").e("Error fetching resto: %s", e.message)
            }
        }
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            try {
                val fetchedMenus = menuUseCase.getMenus().value
                _menus.value = fetchedMenus
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

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty + 1))
            } ?: insertMenu(menuId = menuId, menuQty = 1)
        }
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

                val PAYMENT_METHOD = _orders.value.size - 2
                val TOTAL = orders.value.size - 3

                val payment =
                    if (_orders.value[PAYMENT_METHOD].toString() == "COD" || _orders.value[PAYMENT_METHOD].toString() == "GOPAY") _orders.value[PAYMENT_METHOD].toString() else null

                val total = _orders.value[TOTAL] as Pair<Int, Int>

                if (NetworkUtils.isConnectedToNetwork.value == false) {
                    onResult(R.string.no_internet)
                    return
                }

                if (!payment.isNullOrEmpty() && _orders.value.size > 4 && user != null && USER_ADDRESS != null) {
                    db.runTransaction {
                        USER_ADDRESS?.let { address ->
                            USER_TOKEN?.let { token ->
                                val orderId = getOrderDocumentId()
                                val order = Order(
                                    id = orderId,
                                    userName = user.name,
                                    userId = user.id,
                                    fullAddress = address.address,
                                    restoID = restoID.value,
                                    date = Date(),
                                    paymentMethod = payment,
                                    totalPrice = total.second,
                                    latitude = address.latitude,
                                    longitude = address.longitude,
                                    userToken = token,
                                    restoToken = restoToken.value
                                )
                                val orderToFirebase = OrderResponse.toResponse(order)
                                orderUseCase.setOrder(order.id, orderToFirebase)

                                Timber.tag("orsum").d("ORDERS: ${orders.value}")

                                for (i in 1..<TOTAL) {
                                    val menu = _orders.value[i] as Menu
                                    menu.let {
                                        val orderItem = OrderItem(
                                            id = getOrderItemDocumentId(order.id),
                                            menuName = menu.name,
                                            quantity = menu.orderCartQuantity,
                                            menuPrice = menu.price,
                                            menuImage = menu.image
                                        )
                                        val orderItemResponse =
                                            OrderItemResponse.toResponse(orderItem)
                                        orderUseCase.setOrderItem(
                                            order.id, orderItem.id, orderItemResponse
                                        )
                                    }
                                }

                                viewModelScope.launch {
                                    shipmentUseCase.createShipment(
                                        Shipment(
                                            orderID = orderId, statusDelivery = "On Process", userId = user.id
                                        )
                                    )
                                }

                                sendNotificationToResto(
//                                    onResult
                                )

                                viewModelScope.launch {
                                    cartUseCase.deleteAllMenus()
                                }
                                onResult(null)
                            }
                        }
                    }
                } else {
                    onResult(R.string.failed_checkout_null)
                }
            }

            override fun onFailure(exception: Exception) {
                onResult(R.string.failed_checkout_false)
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

    private fun sendNotificationToResto(
//        onResult: (msg: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = restoToken.value, notification = NotificationBody(
                    title = "New Message", body = "There's new order. Check your Resto App", link = "alfaresto://order"
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
}