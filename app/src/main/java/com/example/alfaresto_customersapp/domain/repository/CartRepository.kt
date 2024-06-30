package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun insertMenu(cartEntity: CartEntity)
    fun getCart(): Flow<List<CartEntity>>
    fun getMenuById(menuId: String): Boolean
    fun deleteMenu(menuId: String)

    suspend fun deleteAllMenus()
}