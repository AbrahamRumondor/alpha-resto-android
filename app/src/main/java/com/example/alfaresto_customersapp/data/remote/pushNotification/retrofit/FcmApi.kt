package com.example.alfaresto_customersapp.data.remote.pushNotification.retrofit

import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )
}