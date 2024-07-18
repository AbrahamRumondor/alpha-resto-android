package com.example.alfaresto_customersapp.domain.usecase.menu

import com.example.alfaresto_customersapp.domain.model.Menu
import kotlinx.coroutines.flow.StateFlow

interface MenuUseCase {
    suspend fun getMenus(): StateFlow<List<Menu>>
    suspend fun getNewMenus(): StateFlow<List<Menu>>
    fun getMenuStock(menuId: String): StateFlow<Int>
    suspend fun updateMenuStock(menuId: String, stock: Int)
}