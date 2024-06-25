package com.example.alfaresto_customersapp.domain.usecase.order

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Order

interface OrderUseCase {
    suspend fun getOrders(): LiveData<List<Order>>
}