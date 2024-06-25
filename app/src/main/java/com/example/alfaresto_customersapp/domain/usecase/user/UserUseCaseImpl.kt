package com.example.alfaresto_customersapp.domain.usecase.user

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : UserUseCase {

    override suspend fun getCurrentUser(): LiveData<User> {
        Log.d("UserUseCaseImpl", "Current user ID: ${authRepository.getCurrentUserID()}")
        return userRepository.getCurrentUser(authRepository.getCurrentUserID())
    }

    override suspend fun getUserAddresses(): LiveData<List<Address>> {
        return userRepository.getUserAddresses(authRepository.getCurrentUserID())
    }
}