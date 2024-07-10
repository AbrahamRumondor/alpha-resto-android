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

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = "",

    @get:PropertyName("status_delivery")
    @set:PropertyName("status_delivery")
    var statusDelivery: String = "",


    @get:PropertyName("is_already_notify_when_50_m")
    @set:PropertyName("is_already_notify_when_50_m")
    var alreadyNotify: Boolean = false
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "")

    companion object {
        fun transform(shipmentResponse: ShipmentResponse): Shipment {
            return Shipment(
                id = shipmentResponse.id,
                orderID = shipmentResponse.orderID,
                statusDelivery = shipmentResponse.statusDelivery,
                userId = shipmentResponse.userId
            )
        }

        fun transform(shipment: Shipment): ShipmentResponse {
            return ShipmentResponse(
                id = shipment.id,
                orderID = shipment.orderID,
                statusDelivery = shipment.statusDelivery,
                userId = shipment.userId
            )
        }
    }
}