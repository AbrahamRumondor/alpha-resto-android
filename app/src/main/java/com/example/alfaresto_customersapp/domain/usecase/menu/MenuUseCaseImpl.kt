package com.example.alfaresto_customersapp.domain.usecase.menu

import android.util.Log
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MenuUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : MenuUseCase {

    override suspend fun getMenus(): StateFlow<List<Menu>> {
        return menuRepository.getMenus()
    }
}