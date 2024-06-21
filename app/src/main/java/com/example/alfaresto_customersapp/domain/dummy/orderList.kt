package com.example.alfaresto_customersapp.domain.dummy

import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem

object orderList {

    val orders = listOf(
        Order(orderDate = "20/06/2024",
            statusDelivery = "Delivered",
            orderTotalPrice = 55000,
            orderItems = listOf(
                OrderItem(menuID = "1", qty = 2),
                OrderItem(menuID = "2", qty = 1),
            )),
        Order(orderDate = "21/06/2024",
            statusDelivery = "On Process",
            orderTotalPrice = 90000,
            orderItems = listOf(
                OrderItem(menuID = "3", qty = 3),
                OrderItem(menuID = "4", qty = 2),
            )),
    )
}