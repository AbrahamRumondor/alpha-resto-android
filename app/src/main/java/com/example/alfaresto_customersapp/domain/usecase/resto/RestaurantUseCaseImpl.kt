package com.example.alfaresto_customersapp.domain.usecase.resto

import com.example.alfaresto_customersapp.domain.model.Restaurant
import com.example.alfaresto_customersapp.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantUseCaseImpl @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) : RestaurantUseCase {

    override suspend fun getRestaurantId(): String {
        return restaurantRepository.getRestaurantId()
    }

    override suspend fun getRestaurantToken(): String {
        return restaurantRepository.getRestaurantToken()
    }

    override suspend fun getRestaurant(): Restaurant? {
        return restaurantRepository.getRestaurant()
    }

    override suspend fun getRestaurantOpenHour(): String {
        return restaurantRepository.getRestaurantOpenHour()
    }

    override suspend fun getRestaurantClosedHour(): String {
        return restaurantRepository.getRestaurantClosedHour()
    }

    override suspend fun isRestaurantClosedTemporary(): Boolean {
        return restaurantRepository.isRestaurantClosedTemporary()
    }
}