package com.example.alfaresto_customersapp.domain.usecase.user

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User

interface UserUseCase {
    suspend fun getCurrentUser(): LiveData<User>
    suspend fun getUserAddresses(): LiveData<List<Address>>
//    suspend fun getUserTokens(): StateFlow<List<Token>>
}