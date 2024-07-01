package com.example.alfaresto_customersapp.domain.usecase.orderHistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject

class OrderHistoryUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) : OrderHistoryUseCase {

    private val _orderHistories = MutableLiveData<List<OrderHistory>>()
    val orderHistories: LiveData<List<OrderHistory>> = _orderHistories

    fun resultOrderHistories(): LiveData<List<OrderHistory>> {
        return orderHistories
    }

    override suspend fun getOrderHistories(orderHistories: (List<OrderHistory>) -> Unit) {
        val uid = authRepository.getCurrentUserID()
        val user = userRepository.getCurrentUser(uid)

        orderRepository.fetchOrders().distinctUntilChangedBy { it.size }.collect { orders ->
            val userAddresses = fetchUserAddresses(uid)

            Log.d("testttt", orders.toString())

            // Filter orders and shipments based on user ID and order IDs
            val myOrders = orders.filter { it.userName == user.value?.name }
            shipmentRepository.getShipments().observeForever { shipments ->
                val myShipments = shipments.filter { shipment ->
                    myOrders.any { it.id == shipment.orderID }
                }

                // Map to order history
                val orderHistories = myOrders.map { order ->
                    val shipment = myShipments.find { it.orderID == order.id }
                    OrderHistory(
                        orderDate = order.date.toString(),
                        orderTotalPrice = order.totalPrice,
                        addressLabel = userAddresses.find { it.address == order.fullAddress }?.label
                            ?: "Unknown",
                        orderStatus = when (shipment?.statusDelivery) {
                            "Delivered" -> OrderStatus.DELIVERED
                            "On Delivery" -> OrderStatus.ON_DELIVERY
                            else -> OrderStatus.ON_PROCESS
                        },
                        orderId = order.id,
                        id = shipment?.id ?: ""
                    )
                }
                Log.d("ORDERHISTORY", orderHistories.toString())

                orderHistories(orderHistories)
            }
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
