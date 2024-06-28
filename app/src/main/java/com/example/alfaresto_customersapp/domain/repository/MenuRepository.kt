package com.example.alfaresto_customersapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Menu

interface MenuRepository {
    suspend fun getMenus(): LiveData<List<Menu>>
}