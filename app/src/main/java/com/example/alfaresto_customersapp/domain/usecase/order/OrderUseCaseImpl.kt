package com.example.alfaresto_customersapp.domain.usecase.order

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import javax.inject.Inject

class OrderUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : OrderUseCase {

    override suspend fun getOrders(): LiveData<List<Order>> {
        return orderRepository.getOrders()
    }
}