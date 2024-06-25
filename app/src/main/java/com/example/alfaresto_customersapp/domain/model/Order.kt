package com.example.alfaresto_customersapp.domain.model

data class Order(
    val id: String = "",
    val userName: String = "",
    val fullAddress: String = "",
    val restoID: String = "",
    val date : String = "",
    val paymentMethod : String = "",
    val totalPrice : Int = 0,
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val orderItems: List<OrderItem> = mutableListOf()
)