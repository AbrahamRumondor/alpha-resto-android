package com.example.alfaresto_customersapp.data.repository

import com.example.alfaresto_customersapp.data.remote.response.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.data.remote.retrofit.FcmApi
import com.example.alfaresto_customersapp.domain.error.DataError
import com.example.alfaresto_customersapp.domain.error.Result
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import javax.inject.Inject

class FcmApiRepositoryImpl @Inject constructor(
    private val fcmService: FcmApi
): FcmApiRepository {
    override suspend fun sendMessage(body: SendMessageDto): Result<String, DataError.Network> {
        return try {
            fcmService.sendMessage(body)
            Result.Success("")
        } catch (e: Exception) {
            Result.Error(handleException(e))
        }
    }

    private fun handleException(e: Exception): DataError.Network {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    408 -> DataError.Network.REQUEST_TIMEOUT
                    429 -> DataError.Network.TOO_MANY_REQUESTS
                    413 -> DataError.Network.PAYLOAD_TOO_LARGE
                    500 -> DataError.Network.SERVER_ERROR
                    else -> DataError.Network.UNKNOWN
                }
            }

            is ConnectException -> DataError.Network.NO_INTERNET
            is IOException -> DataError.Network.NO_INTERNET
            else -> DataError.Network.UNKNOWN
        }
    }

}