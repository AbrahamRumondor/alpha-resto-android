package com.example.alfaresto_customersapp.domain.usecase

import com.example.alfaresto_customersapp.domain.repository.Menus
import kotlinx.coroutines.flow.Flow

interface MenuUseCase {
    fun getMenus(): Flow<Menus>
}