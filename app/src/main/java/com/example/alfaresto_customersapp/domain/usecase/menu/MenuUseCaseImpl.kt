package com.example.alfaresto_customersapp.domain.usecase.menu

import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import javax.inject.Inject

class MenuUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : MenuUseCase {

    override suspend fun getMenus(): LiveData<List<Menu>> {
        return menuRepository.getMenus()
    }
}