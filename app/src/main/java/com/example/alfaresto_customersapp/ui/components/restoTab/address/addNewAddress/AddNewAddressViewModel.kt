package com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ID
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddNewAddressViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
//    private val context: Context
) : ViewModel() {
    private val _chosenLatLng = MutableLiveData<LatLng?>()
    var chosenLatLng: LiveData<LatLng?> = _chosenLatLng

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userUseCase.getCurrentUser().collectLatest { user ->
                USER_ID = user.id
            }
        }
    }

    fun setChosenLatLng(latlng: LatLng?) {
        _chosenLatLng.value = latlng
    }

    suspend fun saveAddressInDatabase(addressLabel: String?, addressDetail: String?) {
        if (!addressLabel.isNullOrEmpty() && !addressDetail.isNullOrEmpty()) {
            chosenLatLng.value?.let { latlng ->
                try {
                    userUseCase.makeNewAddress(
                        Address(
                            label = addressLabel,
                            address = addressDetail,
                            latitude = latlng.latitude,
                            longitude = latlng.longitude
                        )
                    )
                    Toast.makeText(
                        Firebase.firestore.app.applicationContext,
                        "Address added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Timber.tag("test").d(e.toString())
                }
            }
        }
    }
}