package com.example.alfaresto_customersapp.data.remote.retrofit

import com.example.alfaresto_customersapp.data.remote.response.osrm.RouteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OsrmApi {
    @GET("/route/v1/{profile}/{coordinates}")
    fun getRoute(
        @Path("profile") profile: String,
        @Path("coordinates") coordinates: String,
        @Query("alternatives") alternatives: Boolean,
        @Query("steps") steps: Boolean,
        @Query("geometries") geometries: String,
        @Query("overview") overview: String,
        @Query("annotations") annotations: Boolean
    ): Call<RouteResponse>
}