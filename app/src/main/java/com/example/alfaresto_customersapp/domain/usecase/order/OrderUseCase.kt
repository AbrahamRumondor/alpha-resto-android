package com.example.alfaresto_customersapp.domain.usecase.order

import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Chat
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import kotlinx.coroutines.flow.StateFlow

interface OrderUseCase {
    suspend fun getOrders(): StateFlow<List<Order>>
    suspend fun getMyOrders(): StateFlow<List<Order>>
    fun getOrderDocID(): String
    fun getOrderItemDocID(orderId: String): String
    fun setOrder(orderId: String, order: OrderResponse)
    fun setOrderItem(orderId: String, orderItemId: String, orderItem: OrderItemResponse)
    suspend fun getOrderItems(orderId: String): StateFlow<List<OrderItem>>
    suspend fun getOrderByID(orderId: String): Order?
    suspend fun addChatMessage(orderId: String, messageData: Chat): Result<Unit>
    suspend fun getChatMessages(orderId: String): StateFlow<List<Chat>>
}