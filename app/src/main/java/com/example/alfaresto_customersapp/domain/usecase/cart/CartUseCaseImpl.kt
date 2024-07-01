package com.example.alfaresto_customersapp.domain.usecase.cart

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartUseCaseImpl @Inject constructor(
    private val repository: CartRepository
) : CartUseCase {
    override suspend fun insertMenu(cartEntity: CartEntity) {
        repository.insertMenu(cartEntity)
    }

    override fun getCart(): Flow<List<CartEntity>> {
        return repository.getCart()
    }

    override fun getMenuById(menuId: String): Boolean {
        return repository.getMenuById(menuId)
    }

    override suspend fun deleteMenu(menuId: String) {
        repository.deleteMenu(menuId)
    }

    override suspend fun deleteAllMenus() {
        repository.deleteAllMenus()
    }
}