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

    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude: Double = Double.MAX_VALUE,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude: Double = Double.MAX_VALUE,
){
    companion object {
        fun transform(itemResponse: AddressResponse): Address {
            return Address(
                id = itemResponse.id,
                address = itemResponse.address,
                longitude = itemResponse.longitude,
                latitude = itemResponse.latitude,
                label = itemResponse.label
            )
        }
    }
}
