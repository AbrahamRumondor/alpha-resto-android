package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import kotlinx.coroutines.flow.StateFlow

interface OrderRepository {
    suspend fun getOrders(): StateFlow<List<Order>>
    fun fetchOrders(): StateFlow<List<Order>>
    suspend fun getMyOrders(userName: String): StateFlow<List<Order>>
    fun getOrderDocID(): String
    fun getOrderItemDocID(orderId: String): String
    fun setOrder(orderId: String, order: OrderResponse)
    fun setOrderItem(orderId: String, orderItemId: String, orderItem: OrderItemResponse)
    suspend fun getOrderByID(orderId: String): Order?
    suspend fun getOrderItems(orderId: String): StateFlow<List<OrderItem>>
}