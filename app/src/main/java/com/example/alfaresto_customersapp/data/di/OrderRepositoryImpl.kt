package com.example.alfaresto_customersapp.data.di

import android.util.Log
import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class OrderRepositoryImpl @Inject constructor(
    @Named("ordersRef") private val ordersRef: CollectionReference,
    private val firestore: FirebaseFirestore
) : OrderRepository {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    private val orders: StateFlow<List<Order>> = _orders

    override suspend fun getOrders(): StateFlow<List<Order>> {
        try {
            val snapshot = ordersRef.get().await()
            val orderList = snapshot.toObjects(OrderResponse::class.java)
            _orders.value = orderList.map { OrderResponse.transform(it) }

            _orders.value.map {
                Log.d("test", it.toString())
            }
        } catch (e: Exception) {
            _orders.value = emptyList()

            Log.e("OrderHistory orderrepoimpl", "Error fetching orders", e)
        }
        return orders
    }

    override suspend fun getMyOrders(userName: String): StateFlow<List<Order>> {
        try {
            val snapshot = ordersRef.get().await()
            val orderList =
                snapshot.toObjects(OrderResponse::class.java).filter { it.userName == userName }
            _orders.value = orderList.map { OrderResponse.transform(it) }

            _orders.value.map {
                Log.d("test", it.toString())
            }
        } catch (e: Exception) {
            _orders.value = emptyList()
        }
        return orders
    }

    override fun getOrderDocID(): String {
        return ordersRef.document().id
    }

    override fun getOrderItemDocID(orderId: String): String {
        return ordersRef.document(orderId).collection("order_items").document().id
    }

    override fun setOrder(orderId: String, order: OrderResponse) {
        ordersRef.document(orderId).set(order).addOnSuccessListener {
            Log.d(
                "TEST",
                "SUCCESS ON ORDER INSERTION"
            )
        }
            .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER INSERTION") }
    }

    override fun setOrderItem(
        orderId: String,
        orderItemId: String,
        orderItem: OrderItemResponse
    ) {
        ordersRef.document(orderId).collection("order_items").document(orderItemId)
            .set(orderItem).addOnSuccessListener {
                Log.d(
                    "TEST",
                    "SUCCESS ON ORDER ITEM INSERTION"
                )
            }
            .addOnFailureListener { Log.d("TEST", "ERROR ON ORDER ITEM INSERTION") }
    }
}