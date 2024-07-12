package com.example.alfaresto_customersapp.ui.components.orderHistoryPage

import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryUseCase: OrderHistoryUseCase
) : LoadStateViewModel() {

    private val _orderHistories: MutableStateFlow<List<OrderHistory>> =
        MutableStateFlow(emptyList())
    val orderHistories: StateFlow<List<OrderHistory>> = _orderHistories

    init {
        fetchOrderHistories()
    }

    fun setLoadingTrue() {
        setLoading(true)
    }

    private fun fetchOrderHistories() {
        viewModelScope.launch {
            orderHistoryUseCase.getOrderHistories { orderHistories ->
                if (orderHistories.isEmpty()) {
                    setLoading(false)
                    return@getOrderHistories
                }

                Timber.tag("OrderHistory viewmodel")
                    .d("Order histories: $orderHistories")
                _orderHistories.value = orderHistories
                setLoading(false)
            }
        }
    }
}