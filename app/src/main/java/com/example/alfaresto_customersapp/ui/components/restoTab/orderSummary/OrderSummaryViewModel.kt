package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    val address = Address(
        address = "Jl. Alam Sutera Boulevard No.Kav. 21, Pakulonan, Kec. Serpong Utara, Kota Tangerang Selatan, Banten 15325",
        addressLabel = "Kantor",
        addressID = "adfi90sdjaaf98uf",
        latitude = 0.0,
        longitude = 0.0
    )

    private val _menus: MutableStateFlow<List<Menu>> = MutableStateFlow(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    private val _cart: MutableStateFlow<List<CartEntity>> = MutableStateFlow(emptyList())
    val carts: StateFlow<List<CartEntity>> = _cart

    private val _orders: MutableStateFlow<MutableList<Any>> = MutableStateFlow(mutableListOf())
    val orders: StateFlow<List<Any>> = _orders

    private val PAYMENT_METHOD = _orders.value.size - 2
    private val TOTAL = orders.value.size - 3

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

    fun makeOrders(address: Address, orders: List<Menu>, total: Pair<Int, Int>): MutableList<Any> {
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
//    fun saveOrderInDatabase() {
//        val total = cart[TOTAL] as? Pair<Int, Int>
//        db.runTransaction {
//            val order = Order(
//                orderID = getOrderDocumentId(),
//                userID = "-",
//                addressID = "-",
//                restoID = "NrhoLsLLieXFly9dXj7vu2ETi1T2", // nanti buat singleton
//                orderDate = getCurrentDateTime(),
//                orderPaymentMethod = cart[PAYMENT_METHOD].toString(),
//                totalPrice = total?.second ?: -1
//            )
//            db.collection("orders").document(order.orderID)
//                .set(order)
//                .addOnSuccessListener { }
//                .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }
//
//            cart.forEach {
//                when (it) {
//                    is Menu -> {
//
//                        val orderItem = OrderItem(
//                            orderItemId = getOrderItemDocumentId(order.orderID),
//                            menuID = "-",
//                            quantity = it.orderCartQuantity,
//                            menuPrice = it.menuPrice
//                        )
//
//                        db.collection("orders").document(order.orderID)
//                            .collection("order_items").document(orderItem.orderItemId)
//                            .set(order)
//                            .addOnSuccessListener { }
//                            .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }
//                    }
//                }
//            }
//        }
//    }

    private fun getCurrentDateTime(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(currentDate)
    }
}