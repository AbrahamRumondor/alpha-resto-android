package com.example.alfaresto_customersapp.domain.error

import com.example.alfaresto_customersapp.domain.model.User

interface FirestoreCallback {
    fun onSuccess(user: User?)
    fun onFailure(exception: Exception)
}