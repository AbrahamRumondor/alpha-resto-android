package com.example.alfaresto_customersapp.domain.usecase.order

import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Order
import kotlinx.coroutines.flow.StateFlow

interface OrderUseCase {
    suspend fun getOrders(): StateFlow<List<Order>>
    suspend fun getMyOrders(): StateFlow<List<Order>>
    fun getOrderDocID(): String
    fun getOrderItemDocID(orderId: String): String
    fun setOrder(orderId: String, order: OrderResponse)
    fun setOrderItem(orderId: String, orderItemId: String, orderItem: OrderItemResponse)
}