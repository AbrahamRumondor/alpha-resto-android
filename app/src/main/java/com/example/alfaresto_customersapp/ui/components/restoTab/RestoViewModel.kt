package com.example.alfaresto_customersapp.ui.components.restoTab

import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.dummy.MenuDummy

class RestoViewModel : ViewModel() {

    fun getMenuList() = MenuDummy.menuList
}