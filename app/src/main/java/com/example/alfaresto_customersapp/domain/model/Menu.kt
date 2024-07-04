package com.example.alfaresto_customersapp.domain.model

import java.text.NumberFormat
import java.util.Locale

data class Menu(
    // default by database
    val id: String = "",
    val restoId:String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val stock: Int = 0,
    val image: String = "",

    // specific application use
    val isSelected: Boolean = false,
    var orderCartQuantity: Int = 0,
) {
    fun formattedPrice(): String {
        return "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)}"
    }
}
