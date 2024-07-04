package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(): LiveData<List<Order>>
}