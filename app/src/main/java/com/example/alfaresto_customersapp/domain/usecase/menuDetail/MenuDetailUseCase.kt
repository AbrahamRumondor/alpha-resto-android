package com.example.alfaresto_customersapp.domain.usecase.menuDetail

import com.example.alfaresto_customersapp.domain.model.Menu

interface MenuDetailUseCase {
    suspend fun getMenuDetail(menuId: String): Menu
}