package com.example.alfaresto_customersapp.domain.repository

import androidx.paging.PagingData
import com.example.alfaresto_customersapp.domain.model.Menu
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getMenus(): Flow<PagingData<Menu>>
}