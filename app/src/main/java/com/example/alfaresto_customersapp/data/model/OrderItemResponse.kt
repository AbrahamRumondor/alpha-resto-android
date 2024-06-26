package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.google.firebase.firestore.PropertyName

data class OrderItemResponse(
    val id: String,

    @get:PropertyName("menu_id")
    @set:PropertyName("menu_id")
    var menuID: String = "",

    val qty: Int = 0,

    @get:PropertyName("menu_price")
    @set:PropertyName("menu_price")
    var menuPrice: Int = 0
) {
    constructor() : this("", "", 0, 0)

    companion object {
        fun transform(orderItemResponse: OrderItemResponse): OrderItem {
            return OrderItem(
                id = orderItemResponse.id,
                menuID = orderItemResponse.menuID,
                quantity = orderItemResponse.qty,
                menuPrice = orderItemResponse.menuPrice
            )
        }
    }
}
