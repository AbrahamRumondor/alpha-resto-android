package com.example.alfaresto_customersapp.domain.model

data class Address(
    val addressID: String = "",
    val addressLabel: String = "",
    val address: String = "",
    val latitude: Double = Double.MAX_VALUE,
    val longitude: Double = Double.MAX_VALUE,
)
