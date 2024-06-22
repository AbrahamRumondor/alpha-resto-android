package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryUseCase: OrderHistoryUseCase
) : ViewModel() {

    private val _orderHitories: MutableLiveData<List<OrderHistory>> = MutableLiveData()
    val orderHistories: LiveData<List<OrderHistory>> = _orderHitories

    init {
        fetchOrderHistories()
    }

    private fun fetchOrderHistories() {
        viewModelScope.launch {
            orderHistoryUseCase.getOrderHistories().observeForever { orderHistories ->
                if (orderHistories.isEmpty()) {
                    Log.d("ORDER", "Order histories is empty, waiting for data...")
                    // Optionally, you can show a loading state or handle the empty case
                    return@observeForever
                }

                _orderHitories.value = orderHistories
            }
        }
    }
}