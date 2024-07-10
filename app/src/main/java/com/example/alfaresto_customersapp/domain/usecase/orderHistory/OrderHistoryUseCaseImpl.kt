package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderStatus
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject

class OrderHistoryUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) : OrderHistoryUseCase {

    override suspend fun getOrderHistories(orderHistories: (List<OrderHistory>) -> Unit) {
        val uid = authRepository.getCurrentUserID()
        val user = userRepository.getCurrentUser(uid)

        orderRepository.fetchOrders().distinctUntilChangedBy { it.size }.collectLatest { orders ->

            shipmentRepository.getShipments().collectLatest { shipments ->
                val myOrders = orders.filter { it.userName == user.value.name }

                val myShipments = shipments.filter { shipment ->
                    myOrders.any { it.id == shipment.orderID }
                }

                userRepository.getUserAddresses(uid).collectLatest { it ->
                    val orderHistoriesList = myOrders.map { order ->
                        val shipment = myShipments.find { it.orderID == order.id }

                        OrderHistory(
                            orderDate = order.date.toString(),
                            orderTotalPrice = order.totalPrice,
                            addressLabel = it.find { it.address == order.fullAddress }?.label
                                ?: "Unknown",
                            orderStatus = when (shipment?.statusDelivery) {
                                "Delivered" -> OrderStatus.DELIVERED
                                "On Delivery" -> OrderStatus.ON_DELIVERY
                                "On Process" -> OrderStatus.ON_PROCESS
                                else -> OrderStatus.CANCELED
                            },
                            orderId = order.id,
                            id = shipment?.id ?: "",
                            orderNotes = order.notes
                        )
                    }

                    orderHistories(orderHistoriesList)
                }
            }
        }
    }
}