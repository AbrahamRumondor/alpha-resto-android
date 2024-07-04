package com.example.alfaresto_customersapp.domain.model

import java.text.NumberFormat
import java.util.Locale

data class OrderHistory(
    val id: String = "",
    val orderId: String = "",
    val orderDate: String = "",
    val orderTotalPrice: Int = 0,
    val addressLabel: String = "",
    val orderStatus: OrderStatus = OrderStatus.ON_PROCESS
) {
    fun formattedPrice(): String {
        return "Rp ${
            NumberFormat.getNumberInstance(Locale("id", "ID")).format(
                orderTotalPrice
            )
        }"
    }
}
