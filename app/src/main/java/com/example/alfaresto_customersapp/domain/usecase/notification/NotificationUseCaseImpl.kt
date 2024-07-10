package com.example.alfaresto_customersapp.domain.usecase.notification

import com.example.alfaresto_customersapp.data.model.TokenResponse
import com.example.alfaresto_customersapp.data.remote.pushNotification.NotificationBody
import com.example.alfaresto_customersapp.data.remote.pushNotification.SendMessageDto
import com.example.alfaresto_customersapp.domain.error.Result
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
import com.example.alfaresto_customersapp.utils.getText
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class NotificationUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val fcmApiRepository: FcmApiRepository
) : NotificationUseCase {

    override suspend fun sendNotificationToResto(onResult: (msg: String) -> Unit) {
        userRepository.getUserToken(authRepository.getCurrentUserID())
            .addOnSuccessListener { documents ->
                val token = documents.toObjects(TokenResponse::class.java)
                    .firstOrNull()?.userToken

                if (token != null) {
//                    sendMessageToBackend(
//                        message = "There's new order. Check your Resto App",
//                        token = token,
//                        onResult
//                    )
                    Timber.tag("test").d("TOKEN: %s", token)
                }
            }
            .addOnFailureListener {
                Timber.tag("test").d(it, "GAGAL FETCH DATA")
            }.await()
    }

    override suspend fun sendMessageToBackend(
        message: String,
        token: String,
        onResult: (msg: String) -> Unit
    ) {
        val messageDto = SendMessageDto(
            to = token,
            notification = NotificationBody(
                title = "New Order",
                body = message,
                link = "alfaresto://order"
            )
        )

        when (val result = fcmApiRepository.sendMessage(messageDto)) {
            is Result.Success -> {
                onResult(result.data)
            }

            is Result.Error -> {
                onResult(result.error.getText())
            }
        }
    }
}