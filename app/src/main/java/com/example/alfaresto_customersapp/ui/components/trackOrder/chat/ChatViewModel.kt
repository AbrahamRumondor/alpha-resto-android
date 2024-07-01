package com.example.alfaresto_customersapp.ui.components.trackOrder.chat

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _messages = MutableLiveData<List<Pair<String, Boolean>>>()
    val messages: LiveData<List<Pair<String, Boolean>>> get() = _messages

    private val restoId = "NrhoLsLLieXFly9dXj7vu2ETi1T2"

    init {
        firestore = FirebaseFirestore.getInstance()
    }

    fun sendMessage(userId: String, orderId: String, message: String) {
        val chatCollection = firestore.collection("orders")
            .document(orderId)
            .collection("chats")

        Log.d(TAG, "orderId: $orderId")

        val data: HashMap<String, Any> = hashMapOf(
            "message" to message,
            "timestamp" to System.currentTimeMillis(),
            "senderId" to userId,
            "receiverId" to restoId
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
        val chatCollection = firestore.collection("orders")
            .document(orderId)
            .collection("chats")

        chatListener = chatCollection.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("ChatViewModel", "Listen failed.", e)
                return@addSnapshotListener
            }

            val newMessages = mutableListOf<Pair<String, Boolean>>()
            if (snapshots != null) {
                for (doc in snapshots.documentChanges) {
                    if (doc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val message = doc.document.getString("message")
                        val senderId = doc.document.getString("senderId")
                        if (message != null && senderId != null) {
                            val isSent = senderId == FirebaseAuth.getInstance().currentUser?.uid
                            newMessages.add(Pair(message, isSent))
                        }
                    }
                }
                _messages.value = newMessages
            }
        }
    }

    fun removeListener() {
        chatListener?.remove()
    }

    override fun onCleared() {
        super.onCleared()
        removeListener()
    }
}
