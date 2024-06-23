package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Shipment
import com.google.firebase.firestore.PropertyName

data class ShipmentResponse(
    @get:PropertyName("shipment_id")
    @set:PropertyName("shipment_id")
    var shipmentID: String = "",

    @get:PropertyName("order_id")
    @set:PropertyName("order_id")
    var orderID: String = "",

    @get:PropertyName("shipment_status")
    @set:PropertyName("shipment_status")
    var shipmentStatus: String = ""
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "")

    companion object {
        fun transform(shipmentResponse: ShipmentResponse): Shipment {
            return Shipment(
                shipmentID = shipmentResponse.shipmentID,
                orderID = shipmentResponse.orderID,
                shipmentStatus = shipmentResponse.shipmentStatus
            )
        }
    }

}