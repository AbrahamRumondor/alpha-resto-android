package com.example.alfaresto_customersapp.domain.usecase

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Menu

interface MenuUseCase {
    suspend fun getMenus(): LiveData<List<Menu>>
}