package com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.model.AddressResponse
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
    private val cartUseCase: CartUseCase,
    private val userUseCase: UserUseCase,
    private val context: Context
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

    private val db = Firebase.firestore
//    private val addressCollection = db.collection("users").document(USER_ID?:"")
//        .collection("addresses")

    fun setChosenLatLng(latlng: LatLng?) {
        _chosenLatLng.value = latlng
    }

    fun saveAddressInDatabase(addressLabel: String?, addressDetail: String?) {
        if (!addressLabel.isNullOrEmpty() && !addressDetail.isNullOrEmpty()) {
            _chosenLatLng.value?.let { latlng ->
                val newId = getAddressDocumentId()
                try {
                    db.collection("users").document(USER_ID ?: "")
                        .collection("addresses").document(newId).set(
                            AddressResponse.transform(Address(
                                id = newId,
                                label = addressLabel,
                                address = addressDetail,
                                latitude = latlng.latitude,
                                longitude = latlng.longitude
                            ))
                        )
                    Toast.makeText(context, "New Address Successfully Added", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.d("test", e.toString())
                }
            }
        }
    }

    private fun getAddressDocumentId(): String {
        val item = db.collection("users").document(USER_ID ?: "")
            .collection("addresses").document()
        return item.id
    }
}