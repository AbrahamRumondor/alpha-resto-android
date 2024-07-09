package com.example.alfaresto_customersapp.ui.components.loginPage.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(false)
                if (task.isSuccessful) {
                    callback(true)
                }
            }
            .addOnFailureListener { e ->
                Log.e("AuthRepositoryImpl", "Login error: ${e.message}")
                callback(false)
            }
    }
}
