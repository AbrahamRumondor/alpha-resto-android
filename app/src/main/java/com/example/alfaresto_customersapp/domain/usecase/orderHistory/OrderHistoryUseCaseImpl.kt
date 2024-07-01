package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import android.util.Log
import com.example.alfaresto_customersapp.domain.model.Address
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.domain.model.Shipment
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class OrderHistoryUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) : OrderHistoryUseCase {

    private val _orderHistories = MutableStateFlow<List<OrderHistory>>(emptyList())
    private val orderHistories: StateFlow<List<OrderHistory>> = _orderHistories

    private val _myOrderHistory: MutableStateFlow<OrderHistory> = MutableStateFlow(OrderHistory())
    val myOrderHistory: StateFlow<OrderHistory> = _myOrderHistory

    override suspend fun getOrderHistories(): StateFlow<List<OrderHistory>> {
        val uid = authRepository.getCurrentUserID()
        val user = userRepository.getCurrentUser(uid)

        // Fetch orders and shipments
        val orders = fetchOrders()
        val shipments = fetchShipments()
        val userAddresses = fetchUserAddresses(uid)

        // Filter orders and shipments based on user ID and order IDs
        val myOrders = orders.filter { it.userName == user.value.name }
        val myShipments = shipments.filter { shipment ->
            myOrders.any { it.id == shipment.orderID }
        }

        // Map to order history
        val orderHistoriesList = myOrders.map { order ->
            val shipment = myShipments.find { it.orderID == order.id }
            OrderHistory(
                orderId = order.id,
                orderDate = order.date.toString(),
                orderTotalPrice = order.totalPrice,
                addressLabel = userAddresses.find { it.address == order.fullAddress }?.label
                    ?: "Unknown",
                orderStatus = when (shipment?.statusDelivery) {
                    "Delivered" -> OrderStatus.DELIVERED
                    "On Delivery" -> OrderStatus.ON_DELIVERY
                    else -> OrderStatus.ON_PROCESS
                },
                id = shipment?.id ?: ""
            )
        }

        // Update the StateFlow
        _orderHistories.value = orderHistoriesList

        Log.d("OrderHistoryUseCaseImpl 1", "Order Histories: $orderHistories")

        return orderHistories
    }


    private suspend fun fetchOrders(): List<Order> {
        return try {
            orderRepository.getOrders().value
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchShipments(): List<Shipment> {
        return try {
            shipmentRepository.getShipments().value
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchUserAddresses(uid: String): List<Address> {
        return try {
            userRepository.getUserAddresses(uid).value
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getOrderHistoryByOrderID(orderId: String): OrderHistory {
        val uid = authRepository.getCurrentUserID()

        // Fetch orders and shipments
        val orders = fetchOrders()
        val shipments = fetchShipments()
        val userAddresses = fetchUserAddresses(uid)

        // Filter orders and shipments based on user ID and order IDs
        val myOrders = orders.filter { it.id == orderId }
        val myShipments = shipments.filter { shipment ->
            myOrders.any { it.id == shipment.orderID }
        }

        // Map to order history
        val orderHistory = myOrders.map { order ->
            val shipment = myShipments.find { it.orderID == order.id }
            OrderHistory(
                orderId = order.id,
                orderDate = order.date.toString(),
                orderTotalPrice = order.totalPrice,
                addressLabel = userAddresses.find { it.address == order.fullAddress }?.label
                    ?: "Unknown",
                orderStatus = when (shipment?.statusDelivery) {
                    "Delivered" -> OrderStatus.DELIVERED
                    "On Delivery" -> OrderStatus.ON_DELIVERY
                    else -> OrderStatus.ON_PROCESS
                },
                id = shipment?.id ?: ""
            )
        }

        // Update the StateFlow
        _myOrderHistory.value = orderHistory.first()

        Log.d("OrderHistoryUseCaseImpl 1", "Order Histories: $orderHistories")

        return myOrderHistory.value
    }

}
