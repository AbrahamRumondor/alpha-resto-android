package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Order
import com.google.firebase.firestore.PropertyName

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

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude: Double = 0.0,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude: Double = 0.0,

    @get:PropertyName("order_date")
    @set:PropertyName("order_date")
    var date : String = "",

    @get:PropertyName("payment_method")
    @set:PropertyName("payment_method")
    var paymentMethod : String = "",

    @get:PropertyName("total_price")
    @set:PropertyName("total_price")
    var totalPrice : Int = 0,
) {
    companion object {
        fun transform(itemResponse: OrderResponse): Order {
            return Order(
                id = itemResponse.id,
                restoID = itemResponse.restoID,
                fullAddress = itemResponse.fullAddress,
                paymentMethod = itemResponse.paymentMethod,
                totalPrice = itemResponse.totalPrice,
                date = itemResponse.date,
                userName = itemResponse.userName,
                latitude = itemResponse.latitude,
                longitude = itemResponse.longitude
            )
        }
    }
}
