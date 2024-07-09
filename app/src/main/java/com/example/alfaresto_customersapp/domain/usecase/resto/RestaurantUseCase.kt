package com.example.alfaresto_customersapp.domain.usecase.resto

import com.example.alfaresto_customersapp.domain.model.Restaurant

interface RestaurantUseCase {
    suspend fun getRestaurantId(): String
    suspend fun getRestaurantToken(): String
    suspend fun getRestaurant(): Restaurant?
    suspend fun getRestaurantClosedHour(): String
}