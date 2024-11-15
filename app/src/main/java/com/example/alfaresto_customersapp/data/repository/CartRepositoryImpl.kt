package com.example.alfaresto_customersapp.data.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.data.local.room.CartDao
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override suspend fun insertMenu(cartEntity: CartEntity) {
        cartDao.insertMenu(cartEntity)
    }

    override fun getCart(): Flow<List<CartEntity>> {
        return cartDao.getCart()
    }

    override fun getMenuById(menuId: String): Boolean {
        return cartDao.getMenuById(menuId)
    }

    override suspend fun deleteMenu(menuId: String) {
        cartDao.deleteMenu(menuId)
    }

    override suspend fun deleteAllMenus() {
        cartDao.deleteAllMenus()
    }
}