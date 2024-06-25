package com.example.alfaresto_customersapp.domain.usecase.user

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UserUseCase {

    override suspend fun getUserAddresses(uid: String): LiveData<List<Address>> {
        return userRepository.getUserAddresses(uid)
    }
}