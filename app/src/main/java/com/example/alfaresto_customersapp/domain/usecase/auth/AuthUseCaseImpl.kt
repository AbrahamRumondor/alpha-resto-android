package com.example.alfaresto_customersapp.domain.usecase.auth

import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthUseCase {

    override fun getCurrentUserID(): String {
        return authRepository.getCurrentUserID()
    }

    override suspend fun registerUser(email: String, password: String): AuthResult? {
        return authRepository.registerUser(email, password)
    }
}