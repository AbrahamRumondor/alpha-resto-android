package com.example.alfaresto_customersapp.domain.model

data class Address(
    var id: String = "",
    val label: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    val isSelected: Boolean = false
)