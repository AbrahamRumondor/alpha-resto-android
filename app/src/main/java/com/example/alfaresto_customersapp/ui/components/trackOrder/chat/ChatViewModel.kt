package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.remote.pushNotification.NotificationBody
import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.Result
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private var firestore: FirebaseFirestore,
    private val fcmApiRepository: FcmApiRepository,
    private val restaurantUseCase: RestaurantUseCase
) : ViewModel() {

    private var chatListener: ListenerRegistration? = null

    private val _messages = MutableLiveData<List<Pair<String, String>>>()
    val messages: LiveData<List<Pair<String, String>>> get() = _messages

    private val _restoToken: MutableStateFlow<String> = MutableStateFlow("")
    val restoToken: StateFlow<String> = _restoToken

    private val _restoID: MutableStateFlow<String> = MutableStateFlow("")
    val restoID: StateFlow<String> = _restoID

    val restoId = "NrhoLsLLieXFly9dXj7vu2ETi1T2"

    init {
        firestore = FirebaseFirestore.getInstance()
        fetchResto()
    }

    fun sendMessage(userId: String, orderId: String, message: String) {
        val chatCollection = firestore.collection("orders")
            .document(orderId)
            .collection("chats")

        val dateFormatted = Timestamp.now().toDate()

        Log.d(TAG, "orderId: $orderId")

        val data: HashMap<String, Any> = hashMapOf(
            "date_send" to dateFormatted,
            "message" to message,
            "sender_id" to userId,
            "user_name" to "Customer"
        )

        chatCollection.add(data)
            .addOnSuccessListener {
                sendNotificationToResto()
                Log.d(TAG, "Message sent with ID: $orderId")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error sending message", e)
            }
    }

    fun listenForMessages(orderId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            val chatCollection = firestore.collection("orders")
                .document(orderId)
                .collection("chats")

            chatListener = chatCollection.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val messages = mutableListOf<Pair<String, String>>()
                    for (doc in snapshots.documentChanges) {
                        if (doc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                            val message = doc.document.getString("message")
                            val senderId = doc.document.getString("sender_id")
                            if (message != null && senderId != null) {
                                messages.add(Pair(message, senderId))
                            }
                        }
                    }
                    _messages.postValue(messages)
                }
            }
        }
    }

    private fun fetchResto() {
        viewModelScope.launch {
            try {
                val restoID = restaurantUseCase.getRestaurantId()
                _restoID.value = restoID

                val restoToken = restaurantUseCase.getRestaurantToken()
                _restoToken.value = restoToken

                Log.d("RESTO", "Resto Token: $restoToken")
                Log.d("RESTO", "Resto ID: $restoID")
            } catch (e: Exception) {
                Log.e("RESTO", "Error fetching resto: ${e.message}")
            }
        }
    }


    private fun sendNotificationToResto() {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = restoToken.value,
                notification = NotificationBody(
                    title = "New Message",
                    body = "There is a new message from customer #${getUserName()}",
                )
            )

            when (val result = fcmApiRepository.sendMessage(messageDto)) {
                is Result.Success -> {
                    Log.d("test", "FCM SENT")
                }

                is Result.Error -> {
                    Log.d("test", "FCM FAILED")
                }
            }

        }
    }

    fun getUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }

    fun getUserName(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.displayName ?: ""
    }
}
