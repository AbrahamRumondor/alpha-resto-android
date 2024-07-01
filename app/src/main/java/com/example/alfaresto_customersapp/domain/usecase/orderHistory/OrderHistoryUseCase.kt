package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.OrderHistory

interface OrderHistoryUseCase {
    suspend fun getOrderHistories(orderHistories: (List<OrderHistory>) -> Unit)
}