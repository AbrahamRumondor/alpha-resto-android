package com.example.alfaresto_customersapp.domain.repository

interface RestaurantRepository {
    suspend fun getRestaurantId(): String
    suspend fun getRestaurantToken(): String
}