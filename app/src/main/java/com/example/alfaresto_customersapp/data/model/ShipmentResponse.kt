package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Shipment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class ShipmentResponse(
    val id: String,

    @get:PropertyName("order_id")
    @set:PropertyName("order_id")
    var orderID: DocumentReference? = null,

    @get:PropertyName("shipment_status")
    @set:PropertyName("shipment_status")
    var shipmentStatus: String = ""
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", null, "")

    companion object {
        fun transform(shipmentResponse: ShipmentResponse): Shipment {
            return Shipment(
                shipmentID = shipmentResponse.id,
                orderID = shipmentResponse.orderID?.id.orEmpty(),
                shipmentStatus = shipmentResponse.shipmentStatus
            )
        }
    }

}