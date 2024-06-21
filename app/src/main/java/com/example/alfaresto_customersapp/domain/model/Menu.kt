package com.example.alfaresto_customersapp.domain.model

data class Menu(
    // default by database
    val id: String = "",
    val restoId:String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = -1,
    val stock: Int = -1,
    val image: String = "",

    // specific application use
    var isSelected: Boolean = false,
    var orderCartQuantity: Int = 0,
)
