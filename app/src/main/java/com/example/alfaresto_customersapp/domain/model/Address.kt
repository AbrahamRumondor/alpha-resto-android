package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName


data class Address(
    val addressID: String = "",
    val addressLabel: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
