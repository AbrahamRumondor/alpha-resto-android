package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Address
import com.google.firebase.firestore.PropertyName


data class AddressResponse(
    @get:PropertyName("address_id")
    @set:PropertyName("address_id")
    var id: String = "",

    @get:PropertyName("address_label")
    @set:PropertyName("address_label")
    var label: String = "",

    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
){
    companion object {
        fun transform(addressResponse: AddressResponse): Address {
            return Address(
                addressID = addressResponse.id,
                addressLabel = addressResponse.label,
                address = addressResponse.address,
                latitude = addressResponse.latitude,
                longitude = addressResponse.longitude
            )
        }
    }
}
