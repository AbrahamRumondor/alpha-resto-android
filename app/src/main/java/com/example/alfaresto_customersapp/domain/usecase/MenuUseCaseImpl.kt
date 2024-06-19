package com.example.alfaresto_customersapp.domain.usecase

import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.example.alfaresto_customersapp.domain.repository.Menus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MenuUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : MenuUseCase {
    override fun getMenus(): Flow<Menus> = menuRepository.getMenus()
}