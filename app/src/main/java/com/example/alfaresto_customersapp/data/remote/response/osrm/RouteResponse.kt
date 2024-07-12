package com.example.alfaresto_customersapp.data.remote.response.osrm

import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @SerializedName("routes") val routes: List<Route>,
    @SerializedName("waypoints") val waypoints: List<Waypoint>
)

