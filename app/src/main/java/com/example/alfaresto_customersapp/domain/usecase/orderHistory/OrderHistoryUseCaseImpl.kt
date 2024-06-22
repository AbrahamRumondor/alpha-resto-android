package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import javax.inject.Inject

class OrderHistoryUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) : OrderHistoryUseCase {

    override suspend fun getOrderHistories(): LiveData<List<OrderHistory>> = liveData {
        val uid = "amnRLCt7iYGogz6JRxi5"

        // Fetch orders and shipments
        val orders = fetchOrders()
        val shipments = fetchShipments()

        // Filter orders and shipments based on user ID and order IDs
        val myOrders = orders.filter { it.userID == uid }
        val myShipments = shipments.filter { shipment ->
            myOrders.any { it.orderID == shipment.orderID }
        }

        // Map to order history
        val orderHistories = myOrders.map { order ->
            val shipment = myShipments.find { it.orderID == order.orderID }
            OrderHistory(
                orderID = order.orderID,
                orderStatus = shipment?.shipmentStatus ?: "Pending",
            )
        }

        Log.d("OrderHistory UseCaseImpl", "Order histories: $orderHistories")

        emit(orderHistories)
    }

    private suspend fun fetchOrders(): List<Order> {
        return try {
            orderRepository.getOrders().value ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchShipments(): List<Shipment> {
        return try {
            shipmentRepository.getShipments().value ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
