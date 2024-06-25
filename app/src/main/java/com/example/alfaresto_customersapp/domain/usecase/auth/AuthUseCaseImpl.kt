package com.example.alfaresto_customersapp.domain.usecase.auth

import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthUseCase {

    override fun getCurrentUserID(): String {
        return authRepository.getCurrentUserID()
    }
}