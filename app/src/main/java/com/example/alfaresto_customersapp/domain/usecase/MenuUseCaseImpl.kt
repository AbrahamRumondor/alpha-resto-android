package com.example.alfaresto_customersapp.domain.usecase

import android.util.Log
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MenuUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : MenuUseCase {

    override suspend fun getMenus(): StateFlow<List<Menu>> {
        val fetchedMenus = menuRepository.getMenus()
        Log.d("MENU usecaseImpl", "Menus fetched: ${fetchedMenus.value}")
        return fetchedMenus
    }
}