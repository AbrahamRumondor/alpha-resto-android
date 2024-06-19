package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.model.Address
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddressListViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _orderSummaryFlow = MutableStateFlow<List<Address>>(mutableListOf())
    val orderSummaryFlow: StateFlow<List<Address>> = _orderSummaryFlow

    private var selectedAddressIdx: Int? = null

    fun updateAddress(index: Int): Int? {
        var previousSelected: Int? = null
        if (_orderSummaryFlow.value.isNotEmpty()) {
            val allAddresses = _orderSummaryFlow.value.toMutableList()
            allAddresses.forEachIndexed { i, address ->
                if (address.isSelected && i != index) {
                    previousSelected = i
                    allAddresses[i] = address.copy(isSelected = false)
                }
            }

            val selectedAddress = allAddresses[index]
            allAddresses[index] = selectedAddress.copy(isSelected = true)

            _orderSummaryFlow.value = allAddresses
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
                    val address = document.toObject(Address::class.java)
                    addressList.add(address)
                    Log.d("test", "SUCCESS FETCH DATA: ${address.addressID}")
                }
                _orderSummaryFlow.value = addressList
            }
            .addOnFailureListener {
                Log.d("test", "GAGAL FETCH DATA: $it")
            }
    }
}