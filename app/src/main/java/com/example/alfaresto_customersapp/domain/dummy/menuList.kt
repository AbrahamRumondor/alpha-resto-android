package com.example.alfaresto_customersapp.domain.dummy

import com.example.alfaresto_customersapp.domain.model.Menu

object menuList {

    val menus = listOf(
        Menu(menuId = "1", menuName = "Nasi Goreng", menuPrice = 15000),
        Menu(menuId = "2", menuName = "Mie Goreng", menuPrice = 20000),
        Menu(menuId = "3", menuName = "Nasi Uduk", menuPrice = 25000),
        Menu(menuId = "4", menuName = "Mie Ayam", menuPrice = 30000),
    )
}