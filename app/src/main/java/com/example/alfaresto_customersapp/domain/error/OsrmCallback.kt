package com.example.alfaresto_customersapp.domain.error

import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.domain.model.User

interface OsrmCallback {
    fun onSuccess(routeResponse: RouteResponse?)
    fun onFailure(string: String?)
}