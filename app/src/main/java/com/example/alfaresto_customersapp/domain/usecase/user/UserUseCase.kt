package com.example.alfaresto_customersapp.domain.usecase.user

import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserUseCase {
    suspend fun getCurrentUser(): StateFlow<User>
    suspend fun getUserAddresses(): StateFlow<List<Address>>
    suspend fun getUserAddressById(addressId: String): StateFlow<Address>
    suspend fun makeNewAddress(address: Address)
    suspend fun storeUser(uid: String, user: User)

    //    suspend fun getUserTokens(): StateFlow<List<Token>>
    fun saveTokenToDB(uid: String, token: String)
}