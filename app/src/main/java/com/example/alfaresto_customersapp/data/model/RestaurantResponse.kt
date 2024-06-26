package com.example.alfaresto_customersapp.data.model

data class RestaurantResponse(
    val restoID: String = "",
    val restoName: String = "",
    val restoAddress: String = "",
    val restoNoTelp: String = "",
    val restoDescription: String = "",
    val openingTime: String = "",
    val closingTime: String = "",
    val restoImage: String = "",

    val isShown: Boolean = false
)
