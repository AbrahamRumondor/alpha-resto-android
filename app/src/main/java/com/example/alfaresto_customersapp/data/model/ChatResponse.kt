package com.example.alfaresto_customersapp.data.model

import com.example.alfaresto_customersapp.domain.model.Chat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class ChatResponse(
    val id: String = "",

    @get:PropertyName("date_send")
    @set:PropertyName("date_send")
    var dateSend: Timestamp = Timestamp.now(),

    @get:PropertyName("message")
    @set:PropertyName("message")
    var message: String = "",

    @get:PropertyName("sender_id")
    @set:PropertyName("sender_id")
    var senderId: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var userName: String = "",

    @get:PropertyName("read_status")
    @set:PropertyName("read_status")
    var readStatus: Boolean = false,
) {

    constructor() : this("", Timestamp.now(), "", "", "")

    companion object {
        fun transform(itemResponse: ChatResponse): Chat {
            return Chat(
                dateSend = itemResponse.dateSend,
                message = itemResponse.message,
                senderId = itemResponse.senderId,
                userName = itemResponse.userName,
                readStatus = itemResponse.readStatus
            )
        }

        fun transform(item: Chat): ChatResponse {
            return ChatResponse(
                dateSend = item.dateSend,
                message = item.message,
                senderId = item.senderId,
                userName = item.userName,
                readStatus = item.readStatus
            )
        }
    }
}