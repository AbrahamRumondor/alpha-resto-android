package com.example.alfaresto_customersapp.domain.model

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class OrderHistory(
    val id: String = "",
    val orderId: String = "",
    val orderDate: Date = Date(),
    val orderTotalPrice: Int = 0,
    val addressLabel: String = "",
    val orderStatus: OrderStatus = OrderStatus.ON_PROCESS,
    val orderNotes: String = ""
) {
    fun formattedPrice(): String {
        return "Rp ${
            NumberFormat.getNumberInstance(Locale("id", "ID")).format(
                orderTotalPrice
            )
        }"
    }

    fun formattedDate(): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(orderDate)
    }
}
