package com.example.alfaresto_customersapp.ui.components.restoTab

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.LiveData
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestoViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase,
    private val cartUseCase: CartUseCase
) : ViewModel() {

    private val _menus: MutableStateFlow<List<Menu>> = MutableStateFlow(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    private val _cart: MutableStateFlow<List<CartEntity>> = MutableStateFlow(emptyList())
    val cart: StateFlow<List<CartEntity>> = _cart

    // ganti cart ke stateflow

    fun setMenus(list: List<Menu>) {
        _menus.value = list
    }

    init {
        fetchMenus()
        fetchCart()
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

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                // Assuming cartUseCase returns Flow<List<CartEntity>>
                cartUseCase.getCart().collect {
                    it.map {
                        Log.e("CART", "Error fetching cart: ${it.menuId}")
                    }
                    _cart.value = it // Update StateFlow with new data
                }
            } catch (e: Exception) {
                 Log.e("CART", "Error fetching cart: ${e.message}")
            }
        }
    }

    fun insertMenu(menuId: String, menuQty: Int) {
        viewModelScope.launch {
                val cartEntity = CartEntity(menuId = menuId, menuQty = menuQty)
                cartUseCase.insertMenu(cartEntity)
        }
    }

    fun getCart(): Flow<List<CartEntity>> {
        return cartUseCase.getCart()
    }

    fun getOrderByMenuId(list: List<CartEntity>, menuId: String): CartEntity? {
        return list.firstOrNull() { cart ->
            cart.menuId == menuId
        }
    }

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            Log.d("test", "${cart?.menuId} dan ${cart?.menuQty}")
            cart?.let {
                cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty+1))
            } ?: insertMenu(menuId = menuId, menuQty = 1)
        }
    }

    fun decreaseOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                if (cart.menuQty > 0) cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty-1))
            }
        }
    }
}