package com.example.alfaresto_customersapp.ui.components.detailFoodPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.menuDetail.MenuDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFoodViewModel @Inject constructor(
    private val getMenuDetailUseCase: MenuDetailUseCase
) : ViewModel() {

    private val _menu = MutableLiveData<Menu>()
    val menu: LiveData<Menu> get() = _menu

    fun getMenuDetail(menuId: String) {
        viewModelScope.launch {
            val menu = getMenuDetailUseCase.getMenuDetail(menuId)
            _menu.value = menu
        }
    }
}
