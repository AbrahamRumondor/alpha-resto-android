package com.example.alfaresto_customersapp.domain.usecase.order

import android.util.Log
import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Order
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
            userRepository.getCurrentUser(authRepository.getCurrentUserID()).value?.name ?: ""
        Log.d("OrderUseCaseImpl", "getMyOrders: $userName")
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
}