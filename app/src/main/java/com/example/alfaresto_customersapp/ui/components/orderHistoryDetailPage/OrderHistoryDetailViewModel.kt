package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryDetailViewModel @Inject constructor(
    private val orderHistoryUseCase: OrderHistoryUseCase,
    private val userUseCase: UserUseCase,
    private val orderUseCase: OrderUseCase
) : LoadStateViewModel() {
    private val _orderHistory: MutableStateFlow<OrderHistory> = MutableStateFlow(OrderHistory())
    val orderHistory: MutableStateFlow<OrderHistory> = _orderHistory

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: MutableStateFlow<User> = _user

    private val _orderItems: MutableStateFlow<List<OrderItem>> = MutableStateFlow(emptyList())
    val orderItems: MutableStateFlow<List<OrderItem>> = _orderItems

    init {
        fetchUser()
    }

    fun fetchOrderHistory(orderId: String) {
        Log.d("OrderHistoryDetailViewModel", "Order ID: $orderId")
        viewModelScope.launch {
            _orderHistory.value = orderHistoryUseCase.getOrderHistoryByOrderID(orderId)

            Log.d("OrderHistoryDetailViewModel", "Order History: ${orderHistory.value}")
        }
    }


    private fun fetchUser() {
        viewModelScope.launch {
            try {
                userUseCase.getCurrentUser().collectLatest { user ->
                    _user.value = user
                }
            } catch (e: Exception) {
                Log.e("ORDER_HISTORY_DETAIL", "Error fetching user: ${e.message}")
            }
        }
    }

    fun fetchOrderItems(orderId: String) {
        viewModelScope.launch {
            try {
                orderUseCase.getOrderItems(orderId).collectLatest {
                    _orderItems.value = it

                    Log.d("orderhistoryviewmodel", "Order Items: ${orderItems.value}")
                }
            } catch (e: Exception) {
                Log.e("ORDER_HISTORY_DETAIL", "Error fetching order items: ${e.message}")
            }
        }
    }

}