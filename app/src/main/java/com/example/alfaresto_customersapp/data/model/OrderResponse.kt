package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Order
import com.google.firebase.firestore.PropertyName

data class OrderResponse(
    @get:PropertyName("order_id")
    @set:PropertyName("order_id")
    var id: String = "",

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userID: String = "",

    @get:PropertyName("address_id")
    @set:PropertyName("address_id")
    var addressID: String = "",

    @get:PropertyName("resto_id")
    @set:PropertyName("resto_id")
    var restoID: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: String = "",

    @get:PropertyName("payment_method")
    @set:PropertyName("payment_method")
    var paymentMethod: String = "",

    @get:PropertyName("total_price")
    @set:PropertyName("total_price")
    var totalPrice: Int = -1,
) {
    companion object {
        fun transform(itemResponse: OrderResponse): Order {
            return Order(
                id = itemResponse.id,
                restoID = itemResponse.restoID,
                addressID = itemResponse.addressID,
                paymentMethod = itemResponse.paymentMethod,
                totalPrice = itemResponse.totalPrice,
                date = itemResponse.date,
                userID = itemResponse.userID
            )
        }
    }
}
