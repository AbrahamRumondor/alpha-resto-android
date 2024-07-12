package com.example.alfaresto_customersapp.data.repository

import com.example.alfaresto_customersapp.data.model.ChatResponse
import com.example.alfaresto_customersapp.data.model.OrderItemResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Chat
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class OrderRepositoryImpl @Inject constructor(
    @Named("ordersRef") private val ordersRef: CollectionReference
) : OrderRepository {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    private val orders: StateFlow<List<Order>> = _orders

    private val _orderList = MutableStateFlow<List<Order>>(emptyList())
    private val orderList: StateFlow<List<Order>> = _orderList

    private val _chatMessages = MutableStateFlow<List<Chat>>(emptyList())
    private val chatMessages: StateFlow<List<Chat>> = _chatMessages

    private var currentSize = 0

    override suspend fun getOrders(): StateFlow<List<Order>> {
        try {
            val snapshot = ordersRef.get().await()
            val orderList = snapshot.toObjects(OrderResponse::class.java)
            _orders.value = orderList.map { OrderResponse.transform(it) }
        } catch (e: Exception) {
            _orders.value = emptyList()
        }
        return orders
    }

    override fun fetchOrders(): StateFlow<List<Order>> {
        ordersRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                _orders.value = emptyList()
                return@addSnapshotListener
            }

            val orderList = mutableListOf<Order>()
            if (snapshots != null) {
                for (doc in snapshots) {
                    if (doc.exists()) {
                        val orderResponse = doc.toObject(OrderResponse::class.java)
                        orderList.add(OrderResponse.transform(orderResponse))
                    }
                }
            }
            if (currentSize != orderList.size) {
                _orderList.value = orderList
                currentSize = orderList.size
            }

        }
        return orderList
    }

    override suspend fun getMyOrders(userName: String): StateFlow<List<Order>> {
        try {
            val snapshot = ordersRef.get().await()
            val orderList =
                snapshot.toObjects(OrderResponse::class.java).filter { it.userName == userName }
            _orders.value = orderList.map { OrderResponse.transform(it) }
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
            Timber.tag("TEST").d("SUCCESS ON ORDER INSERTION")
        }
            .addOnFailureListener { Timber.tag("TEST").d("ERROR ON ORDER INSERTION") }
    }

    override fun setOrderItem(
        orderId: String,
        orderItemId: String,
        orderItem: OrderItemResponse
    ) {
        ordersRef.document(orderId).collection("order_items").document(orderItemId)
            .set(orderItem).addOnSuccessListener {
                Timber.tag("TEST").d("SUCCESS ON ORDER ITEM INSERTION")
            }
            .addOnFailureListener { Timber.tag("TEST").d("ERROR ON ORDER ITEM INSERTION") }
    }

    override suspend fun getOrderByID(orderId: String): Order? {
        return try {
            val snapshot = ordersRef.document(orderId).get().await()
            val order =
                snapshot.toObject(OrderResponse::class.java)?.let { OrderResponse.transform(it) }

            order
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getOrderItems(orderId: String): StateFlow<List<OrderItem>> {
        val orderItems = MutableStateFlow<List<OrderItem>>(emptyList())

        try {
            val snapshot = ordersRef.document(orderId).collection("order_items").get().await()
            val orderItemList = snapshot.toObjects(OrderItemResponse::class.java)
            orderItems.value = orderItemList.map { OrderItemResponse.transform(it) }
        } catch (e: Exception) {
            orderItems.value = emptyList()
        }
        return orderItems
    }

    override suspend fun addChatMessage(
        orderId: String,
        messageData: Chat
    ) {
        ordersRef.document(orderId).collection("chats").add(ChatResponse.transform(messageData))
            .addOnSuccessListener {
                Timber.tag("OrderRepositoryImpl").d("SUCCESS ON CHAT INSERTION")
            }
            .addOnFailureListener { Timber.tag("OrderRepositoryImpl").d("ERROR ON CHAT INSERTION") }
    }

    override suspend fun getChatMessages(orderId: String): StateFlow<List<Chat>> {
        try {
            ordersRef.document(orderId).collection("chats").orderBy("date_send")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        _chatMessages.value = emptyList()
                        Timber.tag("OrderRepositoryImpl").d("Error getting chat messages")
                        return@addSnapshotListener
                    }

                    val chatList = mutableListOf<Chat>()
                    if (value != null) {
                        for (doc in value) {
                            if (doc.exists()) {
                                val chat = doc.toObject(ChatResponse::class.java).let {
                                    ChatResponse.transform(it)
                                }
                                chatList.add(chat)
                            }
                        }
                    }

                    _chatMessages.value = chatList
                }
        } catch (e: Exception) {
            _chatMessages.value = emptyList()
        }

        return chatMessages
    }
}