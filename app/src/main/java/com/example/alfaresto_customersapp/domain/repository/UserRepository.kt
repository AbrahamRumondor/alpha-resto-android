package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface UserRepository {
    suspend fun getCurrentUser(uid: String): LiveData<User>
    suspend fun getUserAddresses(uid: String): LiveData<List<Address>>
    suspend fun getUserToken(uid: String): Task<QuerySnapshot>
}