package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private var firestore: FirebaseFirestore
) : ViewModel() {

    private var chatListener: ListenerRegistration? = null

    private val _messages = MutableLiveData<List<Pair<String, String>>>()
    val messages: LiveData<List<Pair<String, String>>> get() = _messages

    val restoId = "NrhoLsLLieXFly9dXj7vu2ETi1T2"

    init {
        firestore = FirebaseFirestore.getInstance()
    }

    fun sendMessage(userId: String, orderId: String, message: String) {
        val chatCollection = firestore.collection("orders")
            .document(orderId)
            .collection("chats")

        val dateFormatted = Timestamp.now().toDate()

        Log.d(TAG, "orderId: $orderId")

        val data: HashMap<String, Any> = hashMapOf(
            "date_send" to dateFormatted,
            "message" to message,
            "sender_id" to userId,
            "user_name" to "Customer"
        )

        chatCollection.add(data)
            .addOnSuccessListener {
                Log.d(TAG, "Message sent with ID: $orderId")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error sending message", e)
            }
    }

    fun listenForMessages(orderId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            val chatCollection = firestore.collection("orders")
                .document(orderId)
                .collection("chats")

            chatListener = chatCollection.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val messages = mutableListOf<Pair<String, String>>()
                    for (doc in snapshots.documentChanges) {
                        if (doc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                            val message = doc.document.getString("message")
                            val senderId = doc.document.getString("sender_id")
                            if (message != null && senderId != null) {
                                messages.add(Pair(message, senderId))
                            }
                        }
                    }
                    _messages.postValue(messages)
                }
            }
        }
    }


    fun getUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid ?: ""
    }
}
