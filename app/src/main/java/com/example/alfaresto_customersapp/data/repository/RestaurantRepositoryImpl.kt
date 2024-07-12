package com.example.alfaresto_customersapp.data.repository

import com.example.alfaresto_customersapp.data.model.RestaurantResponse
import com.example.alfaresto_customersapp.domain.model.Restaurant
import com.example.alfaresto_customersapp.domain.repository.RestaurantRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class RestaurantRepositoryImpl @Inject constructor(
    @Named("restosRef") private val restosRef: CollectionReference
) : RestaurantRepository {

    override suspend fun getRestaurantId(): String {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()

            val id = resto?.let { RestaurantResponse.transform(it).id }

            id ?: ""
        } catch (e: Exception) {
            Timber.tag("RestaurantRepositoryImpl").e(e, "Error fetching restaurant")
            ""
        }
    }

    override suspend fun getRestaurantToken(): String {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()

            val token = resto?.let { RestaurantResponse.transform(it).token }

            token ?: ""
        } catch (e: Exception) {
            Timber.tag("RestaurantRepositoryImpl").e(e, "Error fetching restaurant")
            ""
        }
    }

    override suspend fun getRestaurant(): Restaurant? {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()
            resto?.let { RestaurantResponse.transform(it) }
        } catch (e: Exception) {
            Timber.tag("RestaurantRepositoryImpl").e(e, "Error fetching restaurant")
            null
        }
    }

    override suspend fun getRestaurantOpenHour(): String {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()

            val openingTime = resto?.let { RestaurantResponse.transform(it).openingTime }

            openingTime.toString()
        } catch (e: Exception) {
            Timber.tag("RestaurantRepositoryImpl").e(e, "Error fetching restaurant")
            ""
        }
    }

    override suspend fun getRestaurantClosedHour(): String {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()

            val closingTime = resto?.let { RestaurantResponse.transform(it).closingTime }

            closingTime.toString()
        } catch (e: Exception) {
            Timber.tag("RestaurantRepositoryImpl").e(e, "Error fetching restaurant")
            ""
        }
    }

    override suspend fun isRestaurantClosedTemporary(): Boolean {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()

            val isTemporaryClose = resto?.let { RestaurantResponse.transform(it).isTemporaryClose }

            isTemporaryClose ?: false
        } catch (e: Exception) {
            Timber.tag("RestaurantRepositoryImpl").e(e, "Error fetching restaurant")
            false
        }
    }
}