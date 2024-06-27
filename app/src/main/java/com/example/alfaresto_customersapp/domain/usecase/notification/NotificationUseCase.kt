package com.example.alfaresto_customersapp.domain.usecase.notification

interface NotificationUseCase {
    suspend fun sendNotificationToResto(onResult: (msg: String) -> Unit)
    suspend fun sendMessageToBackend(
        message: String,
        token: String,
        onResult: (msg: String) -> Unit
    )
}