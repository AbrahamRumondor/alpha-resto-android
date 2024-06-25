package com.example.alfaresto_customersapp.domain.repository

interface AuthRepository {
    fun getCurrentUserID(): String
}