package com.example.alfaresto_customersapp.domain.model

data class Menu(
    // default by database
    val menuId: String = "",
    val restoId:String = "",
    val menuName: String = "",
    val menuDescription: String = "",
    val menuPrice: Int = 0,
    val menuStock: Int = 0,
    val menuImage: String = "",

    // specific application use
    val isSelected: Boolean = false,
    val orderCartQuantity: Int = 0,
)
