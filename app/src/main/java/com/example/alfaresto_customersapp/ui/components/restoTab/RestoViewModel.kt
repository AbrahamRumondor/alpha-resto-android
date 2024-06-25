package com.example.alfaresto_customersapp.ui.components.restoTab

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestoViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _menus: MutableLiveData<List<Menu>> = MutableLiveData()
    val menus: LiveData<List<Menu>> = _menus

    init {
        fetchMenus()
        fetchCurrentUser()
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            menuUseCase.getMenus().observeForever { menus ->
                if (menus.isEmpty()) {
                    Log.d("Resto viewmodel", "Menus is empty, waiting for data...")
                    // Optionally, you can show a loading state or handle the empty case
                    return@observeForever
                }

                _menus.value = menus
            }
        }
    }

    private fun fetchCurrentUser() {
        val user = authUseCase.getCurrentUserID()
        Log.d("Resto viewmodel", "User: ${user}")
    }

}