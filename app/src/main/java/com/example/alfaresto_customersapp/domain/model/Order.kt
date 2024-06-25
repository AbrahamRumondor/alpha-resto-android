package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName

data class Order(
    val orderID: String = "",
    val userName: String = "",
    val fullAddress: String = "",
    val restoID: String = "",
    val orderDate : String = "",
    val paymentMethod : String = "",
    val totalPrice : Int = 0,
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val orderItems: List<OrderItem> = mutableListOf()
)