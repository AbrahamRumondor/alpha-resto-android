package com.example.alfaresto_customersapp.data.remote.osrm

import com.example.alfaresto_customersapp.data.remote.response.RouteResponse
import com.example.alfaresto_customersapp.data.remote.retrofit.FcmApi
import com.example.alfaresto_customersapp.data.remote.retrofit.OsrmApi
import com.example.alfaresto_customersapp.domain.error.DataError
import com.example.alfaresto_customersapp.domain.error.Result
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.repository.OsrmApiRepository
import retrofit2.Call
import javax.inject.Inject

class OsrmApiRepositoryImpl @Inject constructor(
    private val osrmService: OsrmApi
): OsrmApiRepository {
    override fun getRoute(
        profile: String,
        coordinates: String,
        alternatives: Boolean,
        steps: Boolean,
        geometries: String,
        overview: String,
        annotations: Boolean
    ): Call<RouteResponse> {
        return osrmService.getRoute(
            profile, coordinates, alternatives, steps, geometries, overview, annotations
        )
    }
}