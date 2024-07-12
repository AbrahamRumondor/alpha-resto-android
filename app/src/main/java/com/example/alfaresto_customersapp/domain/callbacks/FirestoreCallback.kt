package com.example.alfaresto_customersapp.domain.callbacks

import com.example.alfaresto_customersapp.domain.model.User

interface FirestoreCallback {
    fun onSuccess(user: User?)
    fun onFailure(exception: Exception)
}