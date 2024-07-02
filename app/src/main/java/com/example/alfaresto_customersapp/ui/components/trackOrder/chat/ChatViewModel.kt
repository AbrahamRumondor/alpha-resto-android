package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.content.ContentValues.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.remote.pushNotification.NotificationBody
import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.Result
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private var firestore: FirebaseFirestore,
    private val fcmApiRepository: FcmApiRepository,
    private val restaurantUseCase: RestaurantUseCase
) : LoadStateViewModel() {

    private var chatListener: ListenerRegistration? = null

    private val _messages = MutableLiveData<List<Pair<String, String>>>()
    val messages: LiveData<List<Pair<String, String>>> get() = _messages

    private val _restoToken: MutableStateFlow<String> = MutableStateFlow("")
    val restoToken: StateFlow<String> = _restoToken

    private val _restoID: MutableStateFlow<String> = MutableStateFlow("")
    val restoID: StateFlow<String> = _restoID

    init {
        firestore = FirebaseFirestore.getInstance()
        fetchResto()
    }

    fun setLoadingTrue() {
        setLoading(true)
    }

    fun setLoadingFalse() {
        setLoading(false)
    }

    fun sendMessage(userId: String, orderId: String, message: String) {
        val chatCollection = firestore.collection("orders")
            .document(orderId)
            .collection("chats")

        val dateFormatted = Timestamp.now().toDate()

        val data: HashMap<String, Any> = hashMapOf(
            "date_send" to dateFormatted,
            "message" to message,
            "sender_id" to userId,
            "user_name" to "Customer"
        )

        chatCollection.add(data)
            .addOnSuccessListener {
                sendNotificationToResto("New message from customer: $message")
                Timber.tag(TAG).d("Message sent with ID: %s", orderId)
            }
            .addOnFailureListener { e ->
                Timber.tag(TAG).w(e, "Error sending message")
            }
    }

    fun listenForMessages(orderId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            val chatCollection = firestore.collection("orders")
                .document(orderId)
                .collection("chats")

            chatListener = chatCollection.orderBy("date_send")
                .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.tag(TAG).w(e, "Listen failed.")
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
            } catch (e: Exception) {
                Timber.tag("RESTO").e("Error fetching resto: %s", e.message)
            }
        }
    }


    private fun sendNotificationToResto(message: String) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = restoToken.value,
                notification = NotificationBody(
                    title = "Customer",
                    body = message,
                )
            )

            when (val result = fcmApiRepository.sendMessage(messageDto)) {
                is Result.Success -> {
                    Timber.tag("test").d("FCM SENT")
                }

                is Result.Error -> {
                    Timber.tag("test").d("FCM FAILED")
                }
            }

        }
    }

    fun getUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }

    private fun getUserName(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.displayName ?: ""
    }
}
