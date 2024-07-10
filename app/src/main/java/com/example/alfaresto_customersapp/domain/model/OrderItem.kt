package com.example.alfaresto_customersapp.domain.model

import java.text.NumberFormat
import java.util.Locale

data class OrderItem(
    val id: String = "",
    val menuName: String = "",
    val quantity: Int = 0,
    val menuPrice: Int = 0,
    val menuImage: String = "",
) {
    fun formattedPrice(): String {
        return "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(menuPrice)}"
    }
}