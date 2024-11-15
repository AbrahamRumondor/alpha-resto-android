package com.example.alfaresto_customersapp.domain.usecase.menu

import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MenuUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : MenuUseCase {

    override suspend fun getMenus(): StateFlow<List<Menu>> {
        return menuRepository.getMenus()
    }

    override suspend fun getNewMenus(): StateFlow<List<Menu>> {
        return menuRepository.getNewMenus()
    }

    override fun getMenuStock(menuId: String, onResult: (Int) -> Unit) {
        return menuRepository.getMenuStock(menuId, onResult)
    }

    override suspend fun updateMenuStock(menuId: String, stock: Int) {
        return menuRepository.updateMenuStock(menuId, stock)
    }
}