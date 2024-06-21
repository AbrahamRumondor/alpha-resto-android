package com.example.alfaresto_customersapp.domain.model

data class OrderItem(
    val id: String = "",
    val menuID: String = "",
    val quantity: Int = -1,
    val menuPrice: Int = -1,
)
