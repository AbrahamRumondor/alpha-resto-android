package com.example.alfaresto_customersapp.domain.usecase.resto

interface RestaurantUseCase {
    suspend fun getRestaurantId(): String
    suspend fun getRestaurantToken(): String
}