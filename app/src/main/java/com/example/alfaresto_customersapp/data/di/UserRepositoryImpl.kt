package com.example.alfaresto_customersapp.data.di

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
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("usersRef") private val usersRef: CollectionReference
) : UserRepository {

    private val _currentUser = MutableStateFlow(User())
    private val currentUser: StateFlow<User> = _currentUser

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    private val addresses: StateFlow<List<Address>> = _addresses

    private val _address = MutableStateFlow(Address())
    private val address: StateFlow<Address> = _address

    override suspend fun getCurrentUser(uid: String): StateFlow<User> {
        try {
            val snapshot = usersRef.get().await()
            val user = snapshot.toObjects(UserResponse::class.java)
                .find { it.id == uid }
                ?.let { UserResponse.transform(it) }

            if (user != null) {
                _currentUser.value = user
            }
        } catch (e: Exception) {
            _currentUser.value = User()
        }
        return currentUser
    }

    override suspend fun getUserAddresses(uid: String): StateFlow<List<Address>> {
        usersRef.document(uid)
            .collection("addresses")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _addresses.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val addressesList = snapshot.toObjects(AddressResponse::class.java)
                        .map { AddressResponse.transform(it) }
                    _addresses.value = addressesList
                }
            }

        return addresses
    }

    override suspend fun getUserAddressById(uid: String, addressId: String): StateFlow<Address> {
        usersRef.document(uid)
            .collection("addresses")
            .document(addressId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _address.value = Address()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val address = snapshot.toObject(AddressResponse::class.java)
                        ?.let { AddressResponse.transform(it) }
                    _address.value = address ?: Address()
                }
            }

        return address
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
            Timber.tag("UserRepositoryImpl").e(e, "Error saving address")
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
                Timber.tag("UserRepositoryImpl").d("Token saved to DB")
            }
            .addOnFailureListener {
                Timber.tag("UserRepositoryImpl").e(it, "Error saving token to DB")
            }
    }

    override suspend fun storeUser(uid: String, user: User) {
        val currentUser = usersRef.document(uid)

        try {
            currentUser.set(UserResponse.transform(user))
        } catch (
            e: Exception
        ) {
            Timber.tag("UserRepositoryImpl").e(
                e, "Error saving user"
            )
        }
    }
}