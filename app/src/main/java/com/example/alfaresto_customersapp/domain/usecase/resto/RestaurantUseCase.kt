package com.example.alfaresto_customersapp.domain.usecase.resto

import android.util.Log
import com.example.alfaresto_customersapp.data.model.RestaurantResponse
import com.example.alfaresto_customersapp.domain.model.Restaurant
import kotlinx.coroutines.tasks.await

interface RestaurantUseCase {
    suspend fun getRestaurantId(): String
    suspend fun getRestaurantToken(): String
    suspend fun getRestaurant(): Restaurant?
}