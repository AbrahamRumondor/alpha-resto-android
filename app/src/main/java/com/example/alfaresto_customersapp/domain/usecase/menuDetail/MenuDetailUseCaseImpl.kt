package com.example.alfaresto_customersapp.domain.usecase.menuDetail

import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import javax.inject.Inject

class MenuDetailUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : MenuDetailUseCase {

    override suspend fun getMenuDetail (menuId: String): Menu {
        return menuRepository.getMenuDetail(menuId) ?: throw Exception("Menu not found")
    }
}