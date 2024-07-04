package com.example.alfaresto_customersapp.domain.service.pushNotification

import com.example.alfaresto_customersapp.domain.model.Token
import com.example.alfaresto_customersapp.utils.user.UserConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToFirestore(token)
        UserConstants.USER_TOKEN = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {

        }
    }

    private fun sendTokenToFirestore(token: String) {
        val db = FirebaseFirestore.getInstance()

        val documentId = db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens").document().id

        val userToken = Token(userToken = token)
        db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens").document(documentId)
            .set(userToken)
            .addOnSuccessListener {
                Timber.tag("test").d("FCM token added to Firestore")
            }
            .addOnFailureListener { e ->
                Timber.tag("test").w(e, "Error adding FCM token to Firestore")
            }
    }
}