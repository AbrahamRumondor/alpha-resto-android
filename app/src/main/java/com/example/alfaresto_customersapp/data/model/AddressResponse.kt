package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Address
import com.google.firebase.firestore.PropertyName

data class AddressResponse(
    @get:PropertyName("address_id")
    @set:PropertyName("address_id")
    var addressID: String = "",

    @get:PropertyName("address_label")
    @set:PropertyName("address_label")
    var addressLabel: String = "",

    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "", 0.0, 0.0)

    companion object {
        fun transform(addressResponse: AddressResponse): Address {
            return Address(
                addressID = addressResponse.addressID,
                addressLabel = addressResponse.addressLabel,
                address = addressResponse.address,
                latitude = addressResponse.latitude,
                longitude = addressResponse.longitude
            )
        }
    }
}