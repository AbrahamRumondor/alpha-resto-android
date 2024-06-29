package com.example.alfaresto_customersapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("geometry") val geometry: String,
    @SerializedName("duration") val duration: Double,
    @SerializedName("distance") val distance: Double
)
