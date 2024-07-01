package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import com.example.alfaresto_customersapp.domain.model.OrderHistory
import kotlinx.coroutines.flow.StateFlow

interface OrderHistoryUseCase {
    suspend fun getOrderHistories(orderHistories: (List<OrderHistory>) -> Unit)
    suspend fun getOrderHistoryByOrderID(orderId: String): OrderHistory
}