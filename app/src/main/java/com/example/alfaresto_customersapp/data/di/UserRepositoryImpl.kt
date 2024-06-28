package com.example.alfaresto_customersapp.data.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.data.model.AddressResponse
import com.example.alfaresto_customersapp.data.model.UserResponse
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("usersRef") private val usersRef: CollectionReference
) : UserRepository {

    private val _currentUser = MutableLiveData<User>()
    private val currentUser: LiveData<User> = _currentUser

    private val _addresses = MutableLiveData<List<Address>>(emptyList())
    private val addresses: LiveData<List<Address>> = _addresses

    override suspend fun getCurrentUser(uid: String): LiveData<User> {
        try {
            val snapshot = usersRef.get().await()
            val user = snapshot.toObjects(UserResponse::class.java)
                .find { it.userID == uid }

            _currentUser.value = user?.let { UserResponse.transform(it) }
        } catch (e: Exception) {
            _currentUser.value = User()

            Log.e("UserRepositoryImpl", "Error fetching user", e)
        }

        Log.d("UserRepositoryImpl", "User: ${currentUser.value}")
        return currentUser
    }

    override suspend fun getUserAddresses(uid: String): LiveData<List<Address>> {
        try {
            val snapshot = usersRef.document(uid).collection("addresses").get().await()
            val addressList = snapshot.toObjects(AddressResponse::class.java)
                .map { AddressResponse.transform(it) }
            _addresses.value = addressList
        } catch (e: Exception) {
            _addresses.value = emptyList()
        }
        return addresses
    }

    override suspend fun getUserToken(uid: String): Task<QuerySnapshot> {
        return usersRef.document(uid)
            .collection("tokens")
            .get()
    }
}