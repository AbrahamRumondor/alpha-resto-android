package com.example.alfaresto_customersapp.ui.components.loginPage.repository

interface AuthRepository {
    fun login(email: String, password: String, callback: (Boolean) -> Unit)
}
