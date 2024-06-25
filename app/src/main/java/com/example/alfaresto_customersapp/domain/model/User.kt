package com.example.alfaresto_customersapp.domain.model

data class User(
    val userID: String = "",
    val userName: String = "",
    val userNoTelp: String = "",
    val userEmail: String = "",
    val userAddress: List<Address> = mutableListOf(),
)
