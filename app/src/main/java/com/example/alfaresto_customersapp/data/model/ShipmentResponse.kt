package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Shipment
import com.google.firebase.firestore.PropertyName

data class ShipmentResponse(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("order_id")
    @set:PropertyName("order_id")
    var orderID: String = "",

    @get:PropertyName("status_delivery")
    @set:PropertyName("status_delivery")
    var statusDelivery: String = ""
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "")

    companion object {
        fun transform(shipmentResponse: ShipmentResponse): Shipment {
            return Shipment(
                id = shipmentResponse.id,
                orderID = shipmentResponse.orderID,
                statusDelivery = shipmentResponse.statusDelivery
            )
        }

        fun transform(shipment: Shipment): ShipmentResponse {
            return ShipmentResponse(
                id = shipment.id,
                orderID = shipment.orderID,
                statusDelivery = shipment.statusDelivery
            )
        }
    }
}