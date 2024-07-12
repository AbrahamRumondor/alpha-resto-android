package com.example.alfaresto_customersapp.domain.callbacks


interface TrackDistanceCallback {
    fun onSuccess(progressPercentage: Int)
    fun onFailure(string: String?)
}