package com.example.alfaresto_customersapp.domain.usecase.user

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address

interface UserUseCase {
    suspend fun getUserAddresses(uid: String): LiveData<List<Address>>
}