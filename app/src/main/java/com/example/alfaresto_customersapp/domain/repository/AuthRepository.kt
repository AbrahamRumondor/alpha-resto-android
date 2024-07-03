package com.example.alfaresto_customersapp.domain.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {
    fun getCurrentUserID(): String
    suspend fun registerUser(email: String, password: String): AuthResult?
}