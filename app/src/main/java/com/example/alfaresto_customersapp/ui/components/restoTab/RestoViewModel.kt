package com.example.alfaresto_customersapp.ui.components.restoTab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestoViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase
) : ViewModel() {

    private val _menus: MutableLiveData<List<Menu>> = MutableLiveData()
    val menus: LiveData<List<Menu>> = _menus

    init {
        fetchMenus()
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            menuUseCase.getMenus().collectLatest {
                if (it.isEmpty()) {
                    return@collectLatest
                }
                _menus.value = it
            }
        }
    }
}