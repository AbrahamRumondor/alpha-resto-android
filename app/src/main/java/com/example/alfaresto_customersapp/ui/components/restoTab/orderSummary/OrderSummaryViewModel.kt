package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderSummaryViewModel : ViewModel() {

    val db = Firebase.firestore

    val address = Address(
        address = "Jl. Alam Sutera Boulevard No.Kav. 21, Pakulonan, Kec. Serpong Utara, Kota Tangerang Selatan, Banten 15325",
        addressLabel = "Kantor",
        addressID = "adfi90sdjaaf98uf",
        latitude = 0.0,
        longitude = 0.0
    )

    val menu1 = Menu(
        menuName = "Pizza AB",
        menuPrice = 15000,
        menuDescription = "daging cincang dan keju",
        orderCartQuantity = 2,
    )

    val menu2 = Menu(
        menuName = "Taco AB",
        menuPrice = 10000,
        menuDescription = "daging cincang dan keju",
        orderCartQuantity = 1,
    )

    val menu3 = Menu(
        menuName = "Kebab AB",
        menuPrice = 5000,
        menuDescription = "daging cincang dan keju",
        orderCartQuantity = 3,
    )

    val cart = mutableListOf(
        address,
        menu1,
        menu2,
        menu3,
        Pair(-1, -1),
        "payment_method",
        "checkout"
    )

    private val PAYMENT_METHOD = cart.size - 2
    private val TOTAL = cart.size - 3

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
    fun saveOrderInDatabase() {
        val total = cart[TOTAL] as? Pair<Int, Int>
        db.runTransaction {
            val order = Order(
                orderID = getOrderDocumentId(),
                userID = "-",
                addressID = "-",
                restoID = "NrhoLsLLieXFly9dXj7vu2ETi1T2", // nanti buat singleton
                orderDate = getCurrentDateTime(),
                orderPaymentMethod = cart[PAYMENT_METHOD].toString(),
                totalPrice = total?.second ?: -1
            )
            db.collection("orders").document(order.orderID)
                .set(order)
                .addOnSuccessListener { }
                .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }

            cart.forEach {
                when (it) {
                    is Menu -> {

                        val orderItem = OrderItem(
                            orderItemId = getOrderItemDocumentId(order.orderID),
                            menuID = "-",
                            quantity = it.orderCartQuantity,
                            menuPrice = it.menuPrice
                        )

                        db.collection("orders").document(order.orderID)
                            .collection("order_items").document(orderItem.orderItemId)
                            .set(order)
                            .addOnSuccessListener { }
                            .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }
                    }
                }
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(currentDate)
    }
}