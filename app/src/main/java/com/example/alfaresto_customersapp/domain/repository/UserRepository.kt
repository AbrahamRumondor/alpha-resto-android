package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    suspend fun getCurrentUser(uid: String): StateFlow<User>
    suspend fun getUserAddresses(uid: String): StateFlow<List<Address>>
    suspend fun getUserAddressById(uid: String, addressId: String): Address
    suspend fun makeNewAddress(uid: String, address: Address)
    suspend fun getUserToken(uid: String): Task<QuerySnapshot>
    fun saveTokenToDB(uid: String, token: String)
    suspend fun storeUser(uid: String, user: User)
}