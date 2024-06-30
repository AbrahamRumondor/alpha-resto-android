package com.example.alfaresto_customersapp.ui.components.listener

import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderHistory

interface OrderHistoryListener {
    fun onOrderClicked(orderHistory: OrderHistory)
}