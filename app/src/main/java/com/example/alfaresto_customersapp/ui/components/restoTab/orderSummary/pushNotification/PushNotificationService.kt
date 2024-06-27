package com.example.alfaresto_customersapp.ui.components.restoTab.orderSummary.pushNotification

import android.util.Log
import com.example.alfaresto_customersapp.domain.model.Token
import com.example.alfaresto_customersapp.utils.user.UserConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService


class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(
            "test",
            "On new token"
        )
        sendTokenToFirestore(token)
        UserConstants.USER_TOKEN = token
    }

    private fun sendTokenToFirestore(token: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val documentId = db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens").document().id

        val userToken = Token(userToken = token)
        db.collection("users").document("amnRLCt7iYGogz6JRxi5")
            .collection("tokens").document(documentId)
            .set(userToken)
            .addOnSuccessListener {
                Log.d(
                    "test",
                    "FCM token added to Firestore"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "test",
                    "Error adding FCM token to Firestore",
                    e
                )
            }
    }
}