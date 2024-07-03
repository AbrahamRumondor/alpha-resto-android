package com.example.alfaresto_customersapp.ui.components.restoTab.address.addressList

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : LoadStateViewModel() {
    private val _userAddresses = MutableStateFlow<List<Address>>(mutableListOf())
    val userAddresses: StateFlow<List<Address>> = _userAddresses

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    private var selectedAddressIdx: Int? = null

    init {
        fetchAllAddresses()
    }

    fun updateAddress(index: Int): Int? {
        var previousSelected: Int? = null
        if (_userAddresses.value.isNotEmpty()) {
            val allAddresses = _userAddresses.value.toMutableList()
            allAddresses.forEachIndexed { i, address ->
                if (address.isSelected && i != index) {
                    previousSelected = i
                    allAddresses[i] = address.copy(isSelected = false)
                }
            }

            val selectedAddress = allAddresses[index]
            allAddresses[index] = selectedAddress.copy(isSelected = true)

            _userAddresses.value = allAddresses
        }
        return previousSelected
    }

    private fun fetchAllAddresses() {
        setLoading(true)
        viewModelScope.launch {
            try {
                userUseCase.getUserAddresses().collectLatest {
                    _userAddresses.value = it
                    setLoading(false)
                }
            } catch (e: Exception) {
                setLoading(false)
            }
        }
    }

    private fun getUserFromDB(callback: FirestoreCallback) {
        viewModelScope.launch {
            try {
                val user = userUseCase.getCurrentUser()
                callback.onSuccess(user.value)
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }
    }

    fun setAnAddress(addressId: String) {
        getUserFromDB(object : FirestoreCallback {
            override fun onSuccess(user: User?) {
                viewModelScope.launch {
                    try {
                        userUseCase.getUserAddressById(addressId).collectLatest {
                            _selectedAddress.value = it
                        }
                        Log.d("address", "Address: ${_selectedAddress.value}")
                    } catch (e: Exception) {
                        Timber.tag("test").d(e, "GAGAL FETCH DATA")
                    }
                }
            }

            override fun onFailure(exception: Exception) {
                Timber.tag("test").d(exception, "On failure addresslistvm DATA")
            }
        })
    }
}