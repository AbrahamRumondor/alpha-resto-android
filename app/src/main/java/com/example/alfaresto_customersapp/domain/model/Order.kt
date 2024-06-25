package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName

data class Order(
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
    var totalPrice : Int = -1,
)
