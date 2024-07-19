package com.example.alfaresto_customersapp.ui.components.chatPage

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.remote.response.pushNotification.NotificationBody
import com.example.alfaresto_customersapp.data.remote.response.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.Result
import com.example.alfaresto_customersapp.domain.model.Chat
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.resto.RestaurantUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.ui.components.trackOrderPage.TrackOrderViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val fcmApiRepository: FcmApiRepository,
    private val restaurantUseCase: RestaurantUseCase,
    private val orderUseCase: OrderUseCase,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private var chatListener: ListenerRegistration? = null

    private val _messages: MutableStateFlow<List<Chat>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Chat>> get() = _messages

    private val _restoToken: MutableStateFlow<String> = MutableStateFlow("")
    private val restoToken: StateFlow<String> = _restoToken

    private val _restoID: MutableStateFlow<String> = MutableStateFlow("")
    val restoID: StateFlow<String> = _restoID

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _orderId = MutableStateFlow("")
    val orderId: StateFlow<String> = _orderId

    init {
        fetchResto()
        getUserId()
        getUser()
        getMessages()
    }

    fun setOrderId(orderId: String) {
        _orderId.value = orderId
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                val orderId = orderId.value
                orderUseCase.addChatMessage(
                    orderId,
                    Chat(
                        dateSend = Timestamp.now(),
                        message = message,
                        senderId = user.value.id,
                        userName = user.value.name
                    )
                )
                sendNotificationToResto("[${user.value.name}]: $message", orderId)
            } catch (e: Exception) {
                Timber.tag(TAG).w(e, "Error sending message")
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            orderId.collectLatest {
                orderUseCase.getChatMessages(orderId.value).collectLatest {
                    _messages.value = it
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

    private fun sendNotificationToResto(message: String, orderId: String) {
        viewModelScope.launch {
            Log.d("notiv", orderId)
            val link = "alfaresto://chat?order_id=${orderId}"

            val newToken = restaurantUseCase.getRestaurantToken()
            _restoToken.value = newToken

            val messageDto = SendMessageDto(
                to = restoToken.value,
                notification = NotificationBody(
                    title = "Customer",
                    body = message,
                    link = link
                )
            )

            when (fcmApiRepository.sendMessage(messageDto)) {
                is Result.Success -> {
                    Timber.tag("test").d("FCM SENT")
                }

                is Result.Error -> {
                    Timber.tag("test").d("FCM FAILED")
                }
            }
        }
    }

    private fun getUserId(): String {
        return authUseCase.getCurrentUserID()
    }

    private fun getUser() {
        viewModelScope.launch {
            _user.value = userUseCase.getCurrentUser().value
        }
    }

    fun updateReadStatus(orderId: String) {
        viewModelScope.launch {
            try {
                orderUseCase.updateReadStatus(orderId, true)
            } catch (e: Exception) {
                Timber.tag("Error").e(e, "Error updating read status")
            }
        }
    }
}
