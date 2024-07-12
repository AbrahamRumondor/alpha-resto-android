package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.data.remote.response.osrm.RouteResponse
import retrofit2.Call

interface OsrmApiRepository {
    fun getRoute(
        profile: String,
        coordinates: String,
        alternatives: Boolean,
        steps: Boolean,
        geometries: String,
        overview: String,
        annotations: Boolean
    ): Call<RouteResponse>
}