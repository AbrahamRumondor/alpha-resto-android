package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.google.firebase.firestore.PropertyName

data class OrderItemResponse(
    @get:PropertyName("order_item_id")
    @set:PropertyName("order_item_id")
    var id: String,

    @get:PropertyName("menu_name")
    @set:PropertyName("menu_name")
    var menuName: String = "",

    @get:PropertyName("quantity")
    @set:PropertyName("quantity")
    var quantity: Int = 0,

    @get:PropertyName("menu_price")
    @set:PropertyName("menu_price")
    var menuPrice: Int = 0,

    @get:PropertyName("menu_image")
    @set:PropertyName("menu_image")
    var menuImage: String = ""
) {
    constructor() : this("", "", 0, 0, "")

    companion object {
        fun transform(orderItemResponse: OrderItemResponse): OrderItem {
            val newOrderItem = OrderItem()
            return newOrderItem.copy(
                id = orderItemResponse.id,
                menuName = orderItemResponse.menuName,
                quantity = orderItemResponse.quantity,
                menuPrice = orderItemResponse.menuPrice,
                menuImage = orderItemResponse.menuImage
            )
        }

        fun toResponse(orderItem: OrderItem): OrderItemResponse {
            val newOrderItem = OrderItemResponse()
            return newOrderItem.copy(
                id = orderItem.id,
                menuName = orderItem.menuName,
                quantity = orderItem.quantity,
                menuPrice = orderItem.menuPrice,
                menuImage = orderItem.menuImage
            )
        }
    }
}
