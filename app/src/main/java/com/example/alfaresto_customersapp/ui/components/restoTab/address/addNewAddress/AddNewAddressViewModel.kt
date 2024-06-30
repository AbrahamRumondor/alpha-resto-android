package com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ID
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewAddressViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _chosenLatLng = MutableLiveData<LatLng?>()
    var chosenLatLng: LiveData<LatLng?> = _chosenLatLng

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userUseCase.getCurrentUser().observeForever { user ->
                if (user == null) {
                    Log.d("TEST", "User is null, waiting for data...")
                    // Optionally, you can show a loading state or handle the null case
                    return@observeForever
                }

                Log.d("Resto viewmodel", "User: ${user.name}")
                USER_ID = user.id
            }
        }
    }

    fun setChosenLatLng(latlng: LatLng?) {
        _chosenLatLng.value = latlng
    }

    fun saveAddressInDatabase(addressLabel: String?, addressDetail: String?) {
        if (!addressLabel.isNullOrEmpty() && !addressDetail.isNullOrEmpty()) {
            chosenLatLng.value?.let { latlng ->
                viewModelScope.launch {
                    val address = Address(
                        label = addressLabel,
                        address = addressDetail,
                        latitude = latlng.latitude,
                        longitude = latlng.longitude
                    )

                    userUseCase.makeNewAddress(address)
                }
            }
        }
    }
}