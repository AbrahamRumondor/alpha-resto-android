package com.example.alfaresto_customersapp.ui.components.trackOrderActivity.chatActivity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var chatListener: ListenerRegistration
    private val restoUserId = "BRRdBhlBmrYWne6qlE5F83dE3372"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        binding.sendButton.setOnClickListener {
            sendMessage()
            listenForMessages()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        chatListener.remove()
    }

    private fun sendMessage() {
        val message = binding.chatInput.text.toString()
        if (message.isNotBlank()) {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            if (userId != null) {
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val data = hashMapOf(
                    "message" to message,
                    "timestamp" to System.currentTimeMillis(),
                    "senderId" to userId,
                    "receiverId" to restoUserId
                )

                val userCollection = firestore.collection("users").document(userId)
                val chatCollection = userCollection.collection("chat").document(currentDate).collection("messages")

                val targetUserCollection = firestore.collection("users").document(restoUserId)
                val targetChatCollection = targetUserCollection.collection("chat").document(currentDate).collection("messages")

                chatCollection.add(data)
                    .addOnSuccessListener {
                        Log.d(TAG, "Chat saved to UID: ${it.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                targetChatCollection.add(data)
                    .addOnSuccessListener {
                        Log.d(TAG, "Chat saved to target UID: ${it.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document to target", e)
                    }

                addMessageToChatView(message, true)
                Log.d(TAG, "6")
                binding.chatInput.text.clear()

                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMessageToChatView(message: String, isSent: Boolean) {
        val layoutId = if (isSent) R.layout.customer_chat else R.layout.resto_chat
        Log.d(TAG, "1")
        val textView = LayoutInflater.from(this)
            .inflate(layoutId, binding.chatLinearLayout, false) as TextView
        Log.d(TAG, "2")
        textView.text = message
        Log.d(TAG, "3")
        binding.chatLinearLayout.addView(textView)
        Log.d(TAG, "4")
    }

    private fun listenForMessages() {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val chatCollection = firestore.collection("users")
                .document(restoUserId)
                .collection("chat")
                .document(currentDate)
                .collection("messages")

            chatListener = chatCollection.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    for (doc in snapshots.documentChanges) {
                        if (doc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                            val message = doc.document.getString("message")
                            val senderId = doc.document.getString("senderId")
                            if (message != null && senderId != userId) {
                                addMessageToChatView(message, false)
                                Log.d(TAG, "5")
                            }
                        }
                    }
                }
            }
        }
    }
}