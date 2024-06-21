package com.example.alfaresto_customersapp.domain.model

data class Order(
    val orderID: String = "",
    val userID: String = "",
    val addressID: String = "",
    val restoID: String = "",
    val orderDate : String = "",
    val orderPaymentMethod : String = "",
    val totalPrice : Int = -1,
    val orderItems: List<OrderItem> = mutableListOf()
)