package com.example.alfaresto_customersapp.ui.components.orderHistoryTab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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

    fun setLoadingTrue() {
        setLoading(true)
    }

    private fun fetchOrderHistories() {
        viewModelScope.launch {
            orderHistoryUseCase.getOrderHistories { orderHistories ->
                if (orderHistories.isEmpty()) {
                    Timber.tag("OrderHistory viewmodel")
                        .d("Order histories is empty, waiting for data...")
                    setLoading(false)
                    return@getOrderHistories
                }

                _orderHistories.value = orderHistories
                setLoading(false)
            }
        }
    }
}