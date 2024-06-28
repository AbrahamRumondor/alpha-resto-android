package com.example.alfaresto_customersapp.domain.model

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
    val orderCartQuantity: Int = 0,
)
