package com.example.alfaresto_customersapp.data.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.alfaresto_customersapp.data.model.AddressResponse
import com.example.alfaresto_customersapp.data.model.UserResponse
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
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

    override suspend fun getCurrentUser(): LiveData<User> {
        return liveData {
            try {
                val snapshot = usersRef.get().await()
                val userList = snapshot.toObjects(UserResponse::class.java)
                    .map { UserResponse.transform(it) }
                _currentUser.value = userList.first()
            } catch (e: Exception) {
                _currentUser.value = User()
            }
            currentUser.value?.let { emit(it) }
        }
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
}