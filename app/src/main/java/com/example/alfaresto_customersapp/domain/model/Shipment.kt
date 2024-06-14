package com.example.alfaresto_customersapp.domain.model

import android.location.Location

data class Shipment(
    val shipmentID: String = "",
    val orderID: String = "",
    val shipmentStatus: String = "",
//    val driverLocation: Location = Location("")
)
