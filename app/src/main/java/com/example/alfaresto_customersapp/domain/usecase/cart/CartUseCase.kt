package com.example.alfaresto_customersapp.domain.usecase.cart

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartUseCase {
    suspend fun insertMenu(cartEntity: CartEntity)
    fun getCart(): Flow<List<CartEntity>>
    fun getMenuById(menuId: String): Boolean
    fun deleteMenu(menuId: String)
}