package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.domain.error.DataError
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.error.OsrmCallback
import com.example.alfaresto_customersapp.domain.error.Result
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

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