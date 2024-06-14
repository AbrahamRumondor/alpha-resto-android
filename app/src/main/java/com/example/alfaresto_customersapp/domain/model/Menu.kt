package com.example.alfaresto_customersapp.domain.model

data class Menu(
    // default by database
    val menuId: String = "",
    val restoId:String = "",
    val menuName: String = "",
    val menuDesc: String = "",
    val menuPrice: Int = -1,
    val menuStock: Int = -1,
    val menuImage: String = "",

    // specific application use
    val isSelected: Boolean = false,
    val orderCartQuantity: Int = 0,
)
