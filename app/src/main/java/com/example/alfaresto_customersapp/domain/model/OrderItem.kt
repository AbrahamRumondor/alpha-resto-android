package com.example.alfaresto_customersapp.domain.model

data class OrderItem(
    val id: String = "",
    val menuName: String = "",
    val quantity: Int = 0,
    val menuPrice: Int = 0,

    val menuImage: String = "",
)