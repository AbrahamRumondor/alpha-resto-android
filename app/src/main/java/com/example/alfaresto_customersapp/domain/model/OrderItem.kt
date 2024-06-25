package com.example.alfaresto_customersapp.domain.model

data class OrderItem(
    val orderItemId: String = "",
    val menuID: String = "",
    val quantity: Int = 0,
    val menuPrice: Int = 0,
)
