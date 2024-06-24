package com.example.alfaresto_customersapp.data.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class OrderRepositoryImpl @Inject constructor(
    @Named("ordersRef") private val ordersRef: CollectionReference
) : OrderRepository {

    private val _orders = MutableLiveData<List<Order>>()
    private val orders: LiveData<List<Order>> = _orders

    override suspend fun getOrders(): LiveData<List<Order>> {
        try {
            val snapshot = ordersRef.get().await()
            val orderList = snapshot.toObjects(OrderResponse::class.java)
            _orders.value = orderList.map { OrderResponse.transform(it) }
        } catch (e: Exception) {
            _orders.value = emptyList()

            Log.e("OrderHistory orderrepoimpl", "Error fetching orders", e)
        }
        return orders
    }
}