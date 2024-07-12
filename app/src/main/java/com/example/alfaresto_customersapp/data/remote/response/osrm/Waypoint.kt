package com.example.alfaresto_customersapp.data.remote.response.osrm

import com.google.gson.annotations.SerializedName

data class Waypoint(
    @SerializedName("location") val location: List<Double>,
    @SerializedName("name") val name: String
)
