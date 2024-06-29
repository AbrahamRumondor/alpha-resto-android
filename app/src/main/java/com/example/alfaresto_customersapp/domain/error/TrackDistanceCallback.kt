package com.example.alfaresto_customersapp.domain.error


interface TrackDistanceCallback {
    fun onSuccess(progressPercentage: Int)
    fun onFailure(string: String?)
}