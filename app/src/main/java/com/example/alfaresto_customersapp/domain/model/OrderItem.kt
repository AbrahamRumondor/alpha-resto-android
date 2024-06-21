package com.example.alfaresto_customersapp.domain.model

data class OrderItem(
    val orderItemId: String = "",
    val menuID: String = "",
    val qty: Int = -1,
)
