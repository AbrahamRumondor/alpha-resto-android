package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.domain.model.Menu
import kotlinx.coroutines.flow.StateFlow

interface MenuRepository {
    suspend fun getMenus(): StateFlow<List<Menu>>
    suspend fun getMenuDetail(menuId: String): Menu?
    suspend fun getNewMenus(): StateFlow<List<Menu>>
}