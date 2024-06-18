package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class ListAllMenuViewModel : ViewModel() {
    val menuList = Pager(
        PagingConfig(pageSize = 10)
    ) {
        MenuPagingSource()
    }.flow.cachedIn(viewModelScope)
}