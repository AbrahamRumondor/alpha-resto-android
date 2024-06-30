package com.example.alfaresto_customersapp.domain.error

import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.google.android.gms.maps.model.LatLng

interface RealtimeLocationCallback {
    fun onSuccess(driverLatLng: LatLng)
    fun onFailure(string: String?)
}