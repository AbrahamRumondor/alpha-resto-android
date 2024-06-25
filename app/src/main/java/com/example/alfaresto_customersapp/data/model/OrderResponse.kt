package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Order
import com.google.firebase.firestore.PropertyName

data class OrderResponse(
    @get:PropertyName("order_id")
    @set:PropertyName("order_id")
    var orderID: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var userName: String = "",

    @get:PropertyName("full_address")
    @set:PropertyName("full_address")
    var fullAddress: String = "",

    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var restoID: String = "",

    @get:PropertyName("order_date")
    @set:PropertyName("order_date")
    var orderDate: String = "",

    @get:PropertyName("payment_method")
    @set:PropertyName("payment_method")
    var paymentMethod: String = "",

    @get:PropertyName("total_price")
    @set:PropertyName("total_price")
    var totalPrice: Int = 0,

    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    @get:PropertyName("order_items")
    @set:PropertyName("order_items")
    var orderItems: List<OrderItemResponse>
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", "", "", "", "", "", 0, 0.0, 0.0, emptyList())

    companion object {
        fun transform(orderResponse: OrderResponse): Order {
            return Order(
                orderID = orderResponse.orderID,
                userName = orderResponse.userName,
                fullAddress = orderResponse.fullAddress,
                restoID = orderResponse.restoID,
                orderDate = orderResponse.orderDate,
                paymentMethod = orderResponse.paymentMethod,
                totalPrice = orderResponse.totalPrice,
                latitude = orderResponse.latitude,
                longitude = orderResponse.longitude,
                orderItems = orderResponse.orderItems.map { OrderItemResponse.transform(it) }
            )
        }
    }
}