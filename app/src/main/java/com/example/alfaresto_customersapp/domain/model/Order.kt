package com.example.alfaresto_customersapp.domain.model

data class Order(
    val orderID: String = "",
    val userID: String = "",
    val addressID: String = "",
    val orderDate : String = "",
    val statusDelivery : String = "",
    val orderTotalPrice : Int = -1,
    val orderItems: List<OrderItem> = mutableListOf()
)