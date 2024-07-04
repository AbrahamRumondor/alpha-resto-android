package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Chat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class ChatResponse(
    val id: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Timestamp = Timestamp.now(),

    @get:PropertyName("message")
    @set:PropertyName("message")
    var message: String = "",

    @get:PropertyName("sender_id")
    @set:PropertyName("sender_id")
    var senderId: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var userName: String = "",
) {
    companion object {
        fun transform(itemResponse: ChatResponse): Chat {
            return Chat(
                date = itemResponse.date,
                message = itemResponse.message,
                senderId = itemResponse.senderId,
                userName = itemResponse.userName
            )
        }
    }
}