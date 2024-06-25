package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.DataError
import com.example.alfaresto_customersapp.domain.error.Result
import retrofit2.http.Body

interface FcmApiRepository {
    suspend fun sendMessage(
        body: SendMessageDto
    ) : Result<String, DataError.Network>
}