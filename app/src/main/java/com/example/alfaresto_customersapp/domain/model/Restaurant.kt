package com.example.alfaresto_customersapp.domain.model

data class Restaurant(
    val id: String = "",
    val email: String = "",
    val address: String = "",
    val noTelp: String = "",
    val description: String = "",
    val openingTime: String = "",
    val closingTime: String = "",
    val image: String = "",
    val token: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var isTemporaryClose: Boolean = false,

    val isShown: Boolean = false
)
