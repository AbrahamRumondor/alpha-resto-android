package com.example.alfaresto_customersapp.domain.usecase.user

import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : UserUseCase {

    override suspend fun getCurrentUser(): StateFlow<User> {
        return userRepository.getCurrentUser(authRepository.getCurrentUserID())
    }

    override suspend fun getUserAddresses(): StateFlow<List<Address>> {
        return userRepository.getUserAddresses(authRepository.getCurrentUserID())
    }

    override suspend fun getUserAddressById(addressId: String): Address {
        return userRepository.getUserAddressById(authRepository.getCurrentUserID(), addressId)
    }

    override suspend fun makeNewAddress(address: Address) {
        userRepository.makeNewAddress(authRepository.getCurrentUserID(), address)
    }

    override fun saveTokenToDB(uid: String, token: String) {
        userRepository.saveTokenToDB(uid, token)
    }
}