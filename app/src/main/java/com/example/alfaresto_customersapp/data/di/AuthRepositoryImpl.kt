package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun getCurrentUserID(): String {
        return auth.currentUser?.uid ?: ""
    }
}