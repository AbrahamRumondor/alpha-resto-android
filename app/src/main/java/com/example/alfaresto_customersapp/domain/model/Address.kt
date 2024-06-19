package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName


data class Address(
    @get:PropertyName("address_id")
    @set:PropertyName("address_id")
    var addressID: String = "",

    @get:PropertyName("address_label")
    @set:PropertyName("address_label")
    var addressLabel: String = "",

    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude: Double = Double.MAX_VALUE,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude: Double = Double.MAX_VALUE,

    var isSelected: Boolean = false
)
