package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.model.AddressResponse
import com.example.alfaresto_customersapp.domain.model.Address
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddressListViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _userAddressFlow = MutableStateFlow<List<Address>>(mutableListOf())
    val userAddressFlow: StateFlow<List<Address>> = _userAddressFlow

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    private var selectedAddressIdx: Int? = null

    fun updateAddress(index: Int): Int? {
        var previousSelected: Int? = null
        if (_userAddressFlow.value.isNotEmpty()) {
            val allAddresses = _userAddressFlow.value.toMutableList()
            allAddresses.forEachIndexed { i, address ->
                if (address.isSelected && i != index) {
                    previousSelected = i
                    allAddresses[i] = address.copy(isSelected = false)
                }
            }

            val selectedAddress = allAddresses[index]
            allAddresses[index] = selectedAddress.copy(isSelected = true)

            _userAddressFlow.value = allAddresses
        }
        return previousSelected
    }

    fun fetchAllAddresses(userId: String) {
        db.collection("users").document(userId)
            .collection("addresses")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("test", "SUCCESS FETCH DATA: $documents")
                val addressList = mutableListOf<Address>()
                for (document in documents) {
                    val address = document.toObject(AddressResponse::class.java)
                    val newAddress = AddressResponse.transform(address)
                    addressList.add(newAddress)
                    Log.d("test", "SUCCESS FETCH DATA: ${address.id}")
                }
                _userAddressFlow.value = addressList
            }
            .addOnFailureListener {
                Log.d("test", "GAGAL FETCH DATA: $it")
            }
    }


    fun setAnAddress(userId: String, addressId: String) {
        viewModelScope.launch {
            try {
                val documentSnapshot = db.collection("users")
                    .document(userId)
                    .collection("addresses")
                    .document(addressId)
                    .get()
                    .await() // Suspends coroutine until document is fetched

                val address = documentSnapshot.toObject(AddressResponse::class.java)
                address?.let {
                    _selectedAddress.value = AddressResponse.transform(address)
                }
            } catch (e: Exception) {
                Log.d("test", "GAGAL FETCH DATA: $e")
            }
        }
    }
}