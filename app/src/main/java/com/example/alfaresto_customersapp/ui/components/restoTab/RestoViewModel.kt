package com.example.alfaresto_customersapp.ui.components.restoTab

import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.domain.model.Response
import com.example.alfaresto_customersapp.domain.repository.MenuList
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestoViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase
) : ViewModel() {

    private var menusResponse: Response<MenuList> = Response.Loading
        private set

    init {
        getMenus()
    }

    private fun getMenus() {

    }
}