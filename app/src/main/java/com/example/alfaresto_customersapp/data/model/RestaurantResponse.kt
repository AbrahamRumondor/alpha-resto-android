package com.example.alfaresto_customersapp.data.model

data class RestaurantResponse(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val noTelp: String = "",
    val description: String = "",
    val openingTime: String = "",
    val closingTime: String = "",
    val image: String = "",

    val isShown: Boolean = false
)
