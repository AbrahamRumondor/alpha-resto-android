package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import javax.inject.Inject

class OrderHistoryUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) : OrderHistoryUseCase {

    override suspend fun getOrderHistories(): LiveData<List<OrderHistory>> = liveData {
        val uid = authRepository.getCurrentUserID()
        val user = userRepository.getCurrentUser(uid)

        // Fetch orders and shipments
        val orders = fetchOrders()
        val shipments = fetchShipments()
        val userAddresses = fetchUserAddresses(uid)

        // Filter orders and shipments based on user ID and order IDs
        val myOrders = orders.filter { it.userName == user.value?.name }
        val myShipments = shipments.filter { shipment ->
            myOrders.any { it.id == shipment.orderID }
        }

        // Map to order history
        val orderHistories = myOrders.map { order ->
            val shipment = myShipments.find { it.orderID == order.id }
            OrderHistory(
                orderDate = order.date,
                orderTotalPrice = order.totalPrice,
                addressLabel = userAddresses.find { it.address == order.fullAddress }?.label
                    ?: "Unknown",
                orderStatus = when (shipment?.statusDelivery) {
                    "delivered" -> OrderStatus.DELIVERED
                    "onDelivery" -> OrderStatus.ON_DELIVERY
                    else -> OrderStatus.ON_PROCESS
                }
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
