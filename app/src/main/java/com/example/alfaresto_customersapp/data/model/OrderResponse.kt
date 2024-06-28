package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Order
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class OrderResponse(
    @get:PropertyName("order_id")
    @set:PropertyName("order_id")
    var id: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var userName: String = "",

    @get:PropertyName("full_address")
    @set:PropertyName("full_address")
    var fullAddress: String = "",

    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var restoID: String = "",

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = "",

    @get:PropertyName("order_date")
    @set:PropertyName("order_date")
    var date: Date = Date(),

    @get:PropertyName("payment_method")
    @set:PropertyName("payment_method")
    var paymentMethod: String = "",

    @get:PropertyName("total_price")
    @set:PropertyName("total_price")
    var totalPrice: Int = 0,

    @get:PropertyName("token")
    @set:PropertyName("token")
    var token: String = "",

    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "", "", "", Date(), "", 0, "",0.0, 0.0)

    companion object {
        fun transform(orderResponse: OrderResponse): Order {
            return Order(
                id = orderResponse.id,
                userName = orderResponse.userName,
                fullAddress = orderResponse.fullAddress,
                restoID = orderResponse.restoID,
                date = orderResponse.date,
                paymentMethod = orderResponse.paymentMethod,
                totalPrice = orderResponse.totalPrice,
                latitude = orderResponse.latitude,
                longitude = orderResponse.longitude,
                userId = orderResponse.userId,
                token = orderResponse.token
            )
        }

        fun toResponse(order: Order): OrderResponse {
            val newOrder = OrderResponse()
            return newOrder.copy(
                id = order.id,
                userName = order.userName,
                fullAddress = order.fullAddress,
                restoID = order.restoID,
                date = order.date,
                paymentMethod = order.paymentMethod,
                totalPrice = order.totalPrice,
                latitude = order.latitude,
                longitude = order.longitude,
                userId = order.userId,
                token = order.token
            )
        }
    }
}