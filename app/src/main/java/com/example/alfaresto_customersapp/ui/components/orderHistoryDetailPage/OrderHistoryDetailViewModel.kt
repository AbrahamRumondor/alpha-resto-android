package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

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
import timber.log.Timber
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
        viewModelScope.launch {
            try {
                setLoading(true)
                orderHistoryUseCase.getOrderHistories { it ->
                    val orderHistory = it.find { it.orderId == orderId }
                    if (orderHistory != null) {
                        _orderHistory.value = orderHistory
                    }
                    setLoading(false)
                }
            } catch (e: Exception) {
                Timber.tag("ORDER_HISTORY_DETAIL").e("Error fetching order history: %s", e.message)
            }
        }
    }


    private fun fetchUser() {
        viewModelScope.launch {
            try {
                setLoading(true)
                userUseCase.getCurrentUser().collectLatest { user ->
                    _user.value = user
                }
                setLoading(false)
            } catch (e: Exception) {
                Timber.tag("ORDER_HISTORY_DETAIL").e("Error fetching user: %s", e.message)
            }
        }
    }

    fun fetchOrderItems(orderId: String) {
        viewModelScope.launch {
            try {
                setLoading(true)
                orderUseCase.getOrderItems(orderId).collectLatest {
                    _orderItems.value = it
                }
                setLoading(false)
            } catch (e: Exception) {
                Timber.tag("ORDER_HISTORY_DETAIL").e("Error fetching order items: %s", e.message)
            }
        }
    }

}