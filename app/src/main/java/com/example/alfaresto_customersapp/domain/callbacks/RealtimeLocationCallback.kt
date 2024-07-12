package com.example.alfaresto_customersapp.domain.callbacks

import com.google.android.gms.maps.model.LatLng

interface RealtimeLocationCallback {
    fun onSuccess(driverLatLng: LatLng)
    fun onFailure(string: String?)
}