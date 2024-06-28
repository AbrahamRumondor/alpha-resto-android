package com.example.alfaresto_customersapp.ui.components.registerPage

import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(email: String, image: String, name: String, noTelp: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { register ->
                if (register.isSuccessful) {
                    val user = auth.currentUser
                    val id = user?.uid ?: return@addOnCompleteListener
                    val hashedPassword = hashPassword(password)
                    val newUser = User(email, id, image, name, noTelp, hashedPassword)
                    addToFirestore(newUser) { success ->
                        onComplete(success)
                    }
                } else {
                    onComplete(false)
                }
            }
    }

    private fun addToFirestore(user: User, onComplete: (Boolean) -> Unit) {
        val userMap = hashMapOf(
            "user_email" to user.userEmail,
            "user_id" to user.userId,
            "user_image" to user.userImage,
            "user_name" to user.userName,
            "user_no_telp" to user.userNoTelp,
            "user_password" to user.userPassword
        )

        firestore.collection("users").document(user.userId)
            .set(userMap)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
