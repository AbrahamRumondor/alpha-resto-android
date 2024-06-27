package com.example.alfaresto_customersapp.domain.model

data class OrderHistory(
    val orderID: String = "",
    val orderDate: String = "",
    val orderTotalPrice: Int = 0,
    val addressLabel: String = "",
    val orderStatus: OrderStatus = OrderStatus.ON_PROCESS
)
