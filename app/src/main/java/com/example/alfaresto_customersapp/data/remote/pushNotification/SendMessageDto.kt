package com.example.alfaresto_customersapp.data.remote.pushNotification

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String,
    val link: String
)