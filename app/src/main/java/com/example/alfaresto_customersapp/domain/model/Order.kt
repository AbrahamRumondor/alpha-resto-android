package com.example.alfaresto_customersapp.domain.model

data class Order(
    val id: String = "",
    val userID: String = "",
    val addressID: String = "",
    val restoID: String = "",
    val date : String = "",
    val paymentMethod : String = "",
    val totalPrice : Int = -1,
    val items: List<OrderItem> = mutableListOf()
)
