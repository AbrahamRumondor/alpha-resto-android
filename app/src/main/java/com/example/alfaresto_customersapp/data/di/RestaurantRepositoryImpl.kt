package com.example.alfaresto_customersapp.data.di

import android.util.Log
import com.example.alfaresto_customersapp.data.model.RestaurantResponse
import com.example.alfaresto_customersapp.domain.model.Restaurant
import com.example.alfaresto_customersapp.domain.repository.RestaurantRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
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

            id?: ""
        } catch (e: Exception) {
            Log.e("RestaurantRepositoryImpl", "Error fetching restaurant", e)
            ""
        }
    }

    override suspend fun getRestaurantToken(): String {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()

            val token = resto?.let { RestaurantResponse.transform(it).token }

            token?: ""
        } catch (e: Exception) {
            Log.e("RestaurantRepositoryImpl", "Error fetching restaurant", e)
            ""
        }
    }

    override suspend fun getRestaurant(): Restaurant? {
        return try {
            val snapshot = restosRef.get().await()
            val resto = snapshot.toObjects(RestaurantResponse::class.java)
                .firstOrNull()
            Log.d("test", "repo ${resto.toString()}")
             resto?.let { RestaurantResponse.transform(it) }
        } catch (e: Exception) {
            Log.e("RestaurantRepositoryImpl", "Error fetching restaurant", e)
            null
        }
    }

}
