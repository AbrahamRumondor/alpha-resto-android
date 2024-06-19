package com.example.alfaresto_customersapp.domain.repository

import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias MenuList = List<Menu>
typealias Menus = Response<MenuList>

interface MenuRepository {
    fun getMenus(): Flow<Menus>
}