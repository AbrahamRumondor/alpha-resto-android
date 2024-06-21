package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Order
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class OrderResponse(
    val id: String = "",

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userID: DocumentReference? = null,

    @get:PropertyName("address_id")
    @set:PropertyName("address_id")
    var addressID: DocumentReference? = null,

    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var restoID: DocumentReference? = null,

    @get:PropertyName("order_date")
    @set:PropertyName("order_date")
    var orderDate: String = "",

    @get:PropertyName("order_payment_method")
    @set:PropertyName("order_payment_method")
    var orderPaymentMethod: String = "",

    @get:PropertyName("total_price")
    @set:PropertyName("total_price")
    var totalPrice: Int = -1,

    @get:PropertyName("order_items")
    @set:PropertyName("order_items")
    var orderItems: List<OrderItemResponse>
) {
    // Public no-argument constructor required by Firestore
    constructor() : this("", null, null, null, "", "", -1, emptyList())

    companion object {
        fun transform(orderResponse: OrderResponse): Order {
            return Order(
                orderID = orderResponse.id,
                userID = orderResponse.userID?.id.orEmpty(),
                addressID = orderResponse.addressID?.id.orEmpty(),
                restoID = orderResponse.restoID?.id.orEmpty(),
                orderDate = orderResponse.orderDate,
                orderPaymentMethod = orderResponse.orderPaymentMethod,
                totalPrice = orderResponse.totalPrice,
                orderItems = orderResponse.orderItems.map { OrderItemResponse.transform(it) }
            )
        }
    }
}