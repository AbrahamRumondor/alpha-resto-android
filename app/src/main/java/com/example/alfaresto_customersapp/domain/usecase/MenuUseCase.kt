package com.example.alfaresto_customersapp.domain.usecase

import com.example.alfaresto_customersapp.domain.model.Menu
import kotlinx.coroutines.flow.StateFlow

interface MenuUseCase {
    suspend fun getMenus(): StateFlow<List<Menu>>
}