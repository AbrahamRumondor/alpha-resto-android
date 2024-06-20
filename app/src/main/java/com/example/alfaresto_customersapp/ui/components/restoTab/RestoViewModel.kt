package com.example.alfaresto_customersapp.ui.components.restoTab

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestoViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase
) : ViewModel() {

    private val _menus: MutableStateFlow<List<Menu>> = MutableStateFlow(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    init {
        fetchMenus()
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            try {
                val fetchedMenus = menuUseCase.getMenus().value
                _menus.value = fetchedMenus
            } catch (e: Exception) {
                Log.e("MENU", "Error fetching menus: ${e.message}")
            }
        }
    }

}