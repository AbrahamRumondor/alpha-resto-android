package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderHistoryDetailViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    private val _order: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    val order: StateFlow<List<Order>> = _order

    private val _orderitems: MutableStateFlow<List<OrderItem>> = MutableStateFlow(emptyList())
    val orderitems: StateFlow<List<OrderItem>> = _orderitems

//    suspend fun getOrderId(orderId: String) {
//        orderUseCase.getOrders()
//            _order.value = it.filter { it.id == orderId }
//        }
//    }

}
