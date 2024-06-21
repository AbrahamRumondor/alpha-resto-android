package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    private val _orders: MutableLiveData<List<Order>> = MutableLiveData(emptyList())
    val orders: MutableLiveData<List<Order>> = _orders

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            try {
                val fetchedOrders = orderUseCase.getOrders().value ?: return@launch
                _orders.value = fetchedOrders
                Log.d("ORDER viewmodel", "Orders fetched: $fetchedOrders")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}