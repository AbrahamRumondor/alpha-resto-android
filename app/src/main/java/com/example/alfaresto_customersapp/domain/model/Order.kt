package com.example.alfaresto_customersapp.domain.model

import java.util.Date

data class Order(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val fullAddress: String = "",
    val restoID: String = "",
    val userToken: String = "",
    val restoToken: String = "",
    val date: Date = Date(),
    val paymentMethod: String = "",
    val totalPrice: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)