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

    val isShown: Boolean = false
)
