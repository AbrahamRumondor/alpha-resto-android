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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("usersRef") private val usersRef: CollectionReference
) : UserRepository {

    private val _currentUser = MutableLiveData<User>()
    private val currentUser: LiveData<User> = _currentUser

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    private val addresses: StateFlow<List<Address>> = _addresses

    override suspend fun getCurrentUser(uid: String): LiveData<User> {
        try {
            val snapshot = usersRef.get().await()
            val user = snapshot.toObjects(UserResponse::class.java)
                .find { it.id == uid }

            _currentUser.value = user?.let { UserResponse.transform(it) }
        } catch (e: Exception) {
            _currentUser.value = User()

            Log.e("UserRepositoryImpl", "Error fetching user", e)
        }

        Log.d("UserRepositoryImpl", "User: ${currentUser.value}")
        return currentUser
    }

    override suspend fun getUserAddresses(uid: String): StateFlow<List<Address>> {
        try {
            val snapshot = usersRef.document(uid)
                .collection("addresses")
                .get()
                .await()

            val addresses = snapshot.toObjects(AddressResponse::class.java)
                .map { AddressResponse.transform(it) }

            _addresses.value = addresses
        } catch (e: Exception) {
            _addresses.value = emptyList()

            Log.e("UserRepositoryImpl", "Error fetching addresses", e)
        }

        Log.d("UserRepositoryImpl", "Addresses: ${addresses.value}")
        return addresses
    }

    override suspend fun getUserAddressById(uid: String, addressId: String): Address {
        return usersRef.document(uid)
            .collection("addresses")
            .document(addressId)
            .get()
            .await()
            .toObject(AddressResponse::class.java)
            ?.let { AddressResponse.transform(it) } ?: Address()
    }

    override suspend fun makeNewAddress(uid: String, address: Address) {
        val currentUser = usersRef.document(uid)

        try {
            val newAddressID = currentUser.collection("addresses").document().id
            address.id = newAddressID
            
            currentUser.collection("addresses")
                .document(newAddressID)
                .set(AddressResponse.transform(address))
                .await()
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "Error saving address", e)
        }
    }

    override suspend fun getUserToken(uid: String): Task<QuerySnapshot> {
        return usersRef.document(uid)
            .collection("tokens")
            .get()
    }

    override fun saveTokenToDB(uid: String, token: String) {
        val currentUser = usersRef.document(uid)

        currentUser.update("token", token)
            .addOnSuccessListener {
                Log.d("UserRepositoryImpl", "Token saved to DB")
            }
            .addOnFailureListener {
                Log.e("UserRepositoryImpl", "Error saving token to DB", it)
            }
    }
}