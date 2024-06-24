package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User

interface UserRepository {
    suspend fun getCurrentUser(): LiveData<User>
    suspend fun getUserAddresses(uid: String): LiveData<List<Address>>
}