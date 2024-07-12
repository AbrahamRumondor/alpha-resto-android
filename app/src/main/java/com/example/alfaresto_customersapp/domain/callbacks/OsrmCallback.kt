package com.example.alfaresto_customersapp.domain.callbacks

import com.example.alfaresto_customersapp.data.remote.response.osrm.RouteResponse

interface OsrmCallback {
    fun onSuccess(routeResponse: RouteResponse?)
    fun onFailure(string: String?)
}