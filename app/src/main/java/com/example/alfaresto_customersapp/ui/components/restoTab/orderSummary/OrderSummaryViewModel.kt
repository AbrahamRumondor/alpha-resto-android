package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.remote.FcmApi
import com.example.alfaresto_customersapp.data.remote.NotificationBody
import com.example.alfaresto_customersapp.data.remote.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.model.Token
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ADDRESS
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OrderSummaryViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase,
    private val cartUseCase: CartUseCase
) : ViewModel() {

    val db = Firebase.firestore

    private val _menus: MutableStateFlow<List<Menu>> = MutableStateFlow(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    private val _cart: MutableStateFlow<List<CartEntity>> = MutableStateFlow(emptyList())
    val carts: StateFlow<List<CartEntity>> = _cart

    private val _orders: MutableStateFlow<MutableList<Any?>> = MutableStateFlow(mutableListOf())
    val orders: StateFlow<List<Any?>> = _orders


    fun setPayment(method: String) {
        _orders.value[orders.value.size - 2] = method
    }

    fun removeOrder(position: Int): Int {
        _orders.value.removeAt(position)
        return _orders.value.size
    }

    fun setMenus(list: List<Menu>) {
        _menus.value = list
    }

    fun makeOrders(
        address: Address?,
        orders: List<Menu>,
        total: Pair<Int, Int>
    ): MutableList<Any?> {
        val orderList = mutableListOf(
            address,
            total,
            "payment_method",
            "checkout"
        )

        orderList.addAll(1, orders)
        _orders.value = orderList

        orderList.map {
            Log.d("test", it.toString())
        }
        return orderList
    }

    init {
        fetchMenus()
        fetchCart()
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            try {
                val fetchedMenus = menuUseCase.getMenus().value
                _menus.value = fetchedMenus
            } catch (e: Exception) {
                Log.e("MENU", "Error fetching menus: ${e.message}")
            }
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                // Assuming cartUseCase returns Flow<List<CartEntity>>
                cartUseCase.getCart().collect {
                    it.map {
                        Log.e("CART", "cart: ${it.menuId}")
                    }
                    _cart.value = it // Update StateFlow with new data
                }
            } catch (e: Exception) {
                Log.e("CART", "Error fetching cart: ${e.message}")
            }
        }
    }

    fun insertMenu(menuId: String, menuQty: Int) {
        viewModelScope.launch {
            val cartEntity = CartEntity(menuId = menuId, menuQty = menuQty)
            cartUseCase.insertMenu(cartEntity)
        }
    }

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            Log.d("test", "${cart?.menuId} dan ${cart?.menuQty}")
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

    private fun getOrderDocumentId(): String {
        val item = db.collection("orders").document()
        return item.id
    }

    private fun getOrderItemDocumentId(orderId: String): String {
        val item = db.collection("orders").document(orderId)
            .collection("order_items").document()
        return item.id
    }

    // TODO 1:userID,addressID,restoID (fetch dr firestore) | 2:menuID (fetch dari firestore)
    fun saveOrderInDatabase(username: String?) {
        val PAYMENT_METHOD = _orders.value.size - 2
        val TOTAL = orders.value.size - 3

        val payment =
            if (_orders.value[PAYMENT_METHOD].toString() == "COD" || _orders.value[PAYMENT_METHOD].toString() == "GOPAY")
                _orders.value[PAYMENT_METHOD].toString() else null

        val total = _orders.value[TOTAL] as Pair<Int, Int> ?: null

        if (!payment.isNullOrEmpty() && total != null && _orders.value.size > 4
            && !username.isNullOrEmpty()
        ) {
            db.runTransaction {
                USER_ADDRESS?.let {
                    val order = Order(
                        id = getOrderDocumentId(),
                        userName = username,
                        fullAddress = it.address,
                        restoID = "NrhoLsLLieXFly9dXj7vu2ETi1T2", // nanti buat singleton
                        date = getCurrentDateTime(),
                        paymentMethod = payment,
                        totalPrice = total.second ?: -1,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                    db.collection("orders").document(order.id)
                        .set(order)
                        .addOnSuccessListener {
                            Log.d(
                                "TEST",
                                "SUCCESS ON ORDER INSERTION"
                            )
                        }
                        .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }

                    for (i in 1..<TOTAL) {
                        val menu = _orders.value[i] as Menu ?: null
                        menu?.let {
                            val orderItem = OrderItem(
                                id = getOrderItemDocumentId(order.id),
                                menuName = menu.name,
                                quantity = menu.orderCartQuantity,
                                menuPrice = menu.price
                            )

                            db.collection("orders").document(order.id)
                                .collection("order_items").document(orderItem.id)
                                .set(orderItem)
                                .addOnSuccessListener {
                                    Log.d(
                                        "TEST",
                                        "SUCCESS ON ORDER ITEM INSERTION"
                                    )
                                }
                                .addOnFailureListener {
                                    Log.d(
                                        "TEST",
                                        "ERROR ON ORDER INSERTION"
                                    )
                                }
                        }
                    }
                }

                sendNotificationToResto()
            }
        }
    }

    fun getUserFromDB(userId: String, callback: FirestoreCallback) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    Log.d("test", "SUCCESS FETCH DATA: $user")
                    callback.onSuccess(user)
                } else {
                    Log.d("test", "No such document")
                    callback.onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("test", "Error fetching data: $exception")
                callback.onFailure(exception)
            }
    }

    private fun getCurrentDateTime(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private val api: FcmApi = Retrofit.Builder()
        .baseUrl("http://192.168.196.165:8081/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    private fun sendNotificationToResto() {
        db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("test", "SUCCESS FETCH DATA: $documents")
                val tokenList = mutableListOf<Token>()
                for (document in documents) {
                    val token = document.toObject(Token::class.java)
                    tokenList.add(token)
                    Log.d("test", "SUCCESS FETCH DATA: ${token.userToken}")
                }
                val latestToken = tokenList[tokenList.size - 1]

                sendMessageToBackend(
                    message = "There's new order. Check your Resto App",
                    token = latestToken.userToken
                )
            }
            .addOnFailureListener {
                Log.d("test", "GAGAL FETCH DATA: $it")
            }
    }

    private fun sendMessageToBackend(message: String, token: String) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = token,
                notification = NotificationBody(
                    title = "New Message",
                    body = message
                )
            )

            try {
                api.sendMessage(messageDto)
            } catch (e: Exception) {
                Log.d("test", e.toString())
            }
        }
    }


}