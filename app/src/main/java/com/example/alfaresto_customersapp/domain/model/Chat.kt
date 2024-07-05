package com.example.alfaresto_customersapp.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val dateSend: Timestamp = Timestamp.now(),
    val message: String = "",
    val senderId: String = "",
    var userName: String = ""
)
