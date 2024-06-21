package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.google.firebase.firestore.PropertyName

data class OrderItemResponse(
    val id: String,

    @get:PropertyName("menu_id")
    @set:PropertyName("menu_id")
    var menuID: String = "",

    val qty: Int = -1,

    @get:PropertyName("menu_price")
    @set:PropertyName("menu_price")
    var menuPrice: Int = -1
) {
    constructor() : this("", "", -1, -1)

    companion object {
        fun transform(orderItemResponse: OrderItemResponse): OrderItem {
            return OrderItem(
                orderItemId = orderItemResponse.id,
                menuID = orderItemResponse.menuID,
                quantity = orderItemResponse.qty,
                menuPrice = orderItemResponse.menuPrice
            )
        }
    }
}
