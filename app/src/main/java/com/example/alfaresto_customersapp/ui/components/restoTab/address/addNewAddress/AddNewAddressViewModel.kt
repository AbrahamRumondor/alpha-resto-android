package com.example.alfaresto_customersapp.ui.components.restoTab.address.addNewAddress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_ID
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AddNewAddressViewModel : ViewModel() {
    private val _chosenLatLng = MutableLiveData<LatLng?>()

    var chosenLatLng: LiveData<LatLng?> = _chosenLatLng

    private val db = Firebase.firestore
    private val addressCollection = db.collection("users").document(USER_ID)
        .collection("addresses")

    fun setChosenLatLng(latlng: LatLng?) {
        _chosenLatLng.value = latlng
    }

    fun saveAddressInDatabase(addressLabel: String?, addressDetail: String?) {
        if (!addressLabel.isNullOrEmpty() && !addressDetail.isNullOrEmpty()) {
            _chosenLatLng.value?.let { latlng->
                val newId = getAddressDocumentId()
                try {
                    addressCollection.document(newId).set(
                        Address(
                            id = newId,
                            label = addressLabel,
                            address = addressDetail,
                            latitude = latlng.latitude,
                            longitude = latlng.longitude
                        )
                    )
                } catch (e: Exception) {
                    Log.d("test", e.toString())
                }
            }
        }
    }

    private fun getAddressDocumentId(): String {
        val item = db.collection("users").document(USER_ID)
            .collection("addresses").document()
        return item.id
    }
}