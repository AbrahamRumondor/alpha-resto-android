package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import javax.inject.Inject

class OrderHistoryUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) : OrderHistoryUseCase {

    override suspend fun getOrderHistories(): LiveData<List<OrderHistory>> = liveData {
        val uid = "amnRLCt7iYGogz6JRxi5" // get from firebase auth uid

        // Fetch orders and shipments
        val orders = fetchOrders()
        val shipments = fetchShipments()
        val userAddresses = fetchUserAddresses(uid)

        // Filter orders and shipments based on user ID and order IDs
        val myOrders = orders.filter { it.userID == uid }
        val myShipments = shipments.filter { shipment ->
            myOrders.any { it.orderID == shipment.orderID }
        }

        // Map to order history
        val orderHistories = myOrders.map { order ->
            val shipment = myShipments.find { it.orderID == order.orderID }
            OrderHistory(
                orderDate = order.orderDate,
                orderTotalPrice = order.totalPrice,
                addressLabel = userAddresses.find { it.addressID == order.addressID }?.addressLabel
                    ?: "Unknown",
                orderStatus = shipment?.shipmentStatus ?: "Pending",
            )
        }

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

    private suspend fun fetchUserAddresses(uid: String): List<Address> {
        return try {
            userRepository.getUserAddresses(uid).value ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
