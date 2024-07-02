package com.example.alfaresto_customersapp.ui.components.loginPage.repository

import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class AuthRepositoryImpl : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    Timber.tag("AuthRepositoryImpl").e("Login failed: %s", task.exception?.message)
                    callback(false)
                }
            }
            .addOnFailureListener { e ->
                Timber.tag("AuthRepositoryImpl").e("Login error: %s", e.message)
                callback(false)
            }
    }
}
