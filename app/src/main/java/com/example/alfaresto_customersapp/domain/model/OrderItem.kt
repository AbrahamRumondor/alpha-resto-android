package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.firestore.PropertyName

data class OrderItem(
    @get:PropertyName("order_item_id")
    @set:PropertyName("order_item_id")
    var id: String = "",

    @get:PropertyName("menu_id")
    @set:PropertyName("menu_id")
    var menuID: String = "",

    @get:PropertyName("quantity")
    @set:PropertyName("v")
    var quantity: Int = -1,

    @get:PropertyName("menu_price")
    @set:PropertyName("menu_price")
    var menuPrice: Int = -1,
)
