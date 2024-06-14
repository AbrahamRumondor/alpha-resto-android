package com.example.alfaresto_customersapp.domain.model

data class Restaurant(
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
