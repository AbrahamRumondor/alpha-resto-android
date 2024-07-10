package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.domain.model.Restaurant

interface RestaurantRepository {
    suspend fun getRestaurantId(): String
    suspend fun getRestaurantToken(): String
    suspend fun getRestaurant(): Restaurant?
    suspend fun getRestaurantOpenHour(): String
    suspend fun getRestaurantClosedHour(): String
    suspend fun isRestaurantClosedTemporary(): Boolean
}