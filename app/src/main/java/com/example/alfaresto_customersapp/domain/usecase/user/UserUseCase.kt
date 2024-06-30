package com.example.alfaresto_customersapp.domain.usecase.user

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserUseCase {
    suspend fun getCurrentUser(): LiveData<User>
    suspend fun getUserAddresses(): StateFlow<List<Address>>
    suspend fun getUserAddressById(addressId: String): Address
    suspend fun makeNewAddress(address: Address)
    //    suspend fun getUserTokens(): StateFlow<List<Token>>
    fun saveTokenToDB(uid: String, token: String)
}