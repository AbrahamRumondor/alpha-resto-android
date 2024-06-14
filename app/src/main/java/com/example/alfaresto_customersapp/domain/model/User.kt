package com.example.alfaresto_customersapp.domain.model

data class User(
    val userID: String = "",
    val userName: String = "",
    val userNoPhone: String = "",
    val userEmail: String = "",
    val userPassword: String = "",
    val userAddress: List<Address> = mutableListOf(),
)
