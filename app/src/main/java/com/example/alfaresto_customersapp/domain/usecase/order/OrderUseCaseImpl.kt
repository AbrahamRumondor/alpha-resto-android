package com.example.alfaresto_customersapp.domain.usecase.order

import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Chat
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class OrderUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : OrderUseCase {

    override suspend fun getOrders(): StateFlow<List<Order>> {
        return orderRepository.getOrders()
    }

    override suspend fun getMyOrders(): StateFlow<List<Order>> {
        val userName =
            userRepository.getCurrentUser(authRepository.getCurrentUserID()).value.name
        return orderRepository.getMyOrders(userName)
    }

    override fun getOrderDocID(): String {
        return orderRepository.getOrderDocID()
    }

    override fun getOrderItemDocID(orderId: String): String {
        return orderRepository.getOrderItemDocID(orderId)
    }

    override fun setOrder(orderId: String, order: OrderResponse) {
        orderRepository.setOrder(orderId, order)
    }

    override fun setOrderItem(orderId: String, orderItemId: String, orderItem: OrderItemResponse) {
        orderRepository.setOrderItem(orderId, orderItemId, orderItem)
    }

    override suspend fun getOrderItems(orderId: String): StateFlow<List<OrderItem>> {
        return orderRepository.getOrderItems(orderId)
    }

    override suspend fun getOrderByID(orderId: String): Order? {
        return orderRepository.getOrderByID(orderId)
    }

    override suspend fun addChatMessage(
        orderId: String,
        messageData: Chat
    ): Result<Unit> {
        return try {
            orderRepository.addChatMessage(orderId, messageData)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}