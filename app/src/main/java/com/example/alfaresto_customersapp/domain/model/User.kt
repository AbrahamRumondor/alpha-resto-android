package com.example.alfaresto_customersapp.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val address: List<Address> = mutableListOf()
)
