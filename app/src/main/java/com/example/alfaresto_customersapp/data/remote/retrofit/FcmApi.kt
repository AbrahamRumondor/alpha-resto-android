package com.example.alfaresto_customersapp.data.remote.retrofit

import com.example.alfaresto_customersapp.data.remote.response.pushNotification.SendMessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/send-android")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )
}