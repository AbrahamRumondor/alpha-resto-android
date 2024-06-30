package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryUseCase: OrderHistoryUseCase
) : LoadStateViewModel() {

    private val _orderHistories: MutableLiveData<List<OrderHistory>> = MutableLiveData()
    val orderHistories: LiveData<List<OrderHistory>> = _orderHistories

    init {
        fetchOrderHistories()
    }

    private fun fetchOrderHistories() {
        viewModelScope.launch {
            orderHistoryUseCase.getOrderHistories().observeForever { orderHistories ->
                setLoading(true)
                if (orderHistories.isEmpty()) {
                    Log.d("OrderHistory viewmodel", "Order histories is empty, waiting for data...")
                    setLoading(false)
                    return@observeForever
                }

                _orderHistories.value = orderHistories
                setLoading(false)
            }
        }
    }
}