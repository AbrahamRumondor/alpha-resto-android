package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.pagingSource.MenuPagingSource
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListAllMenuViewModel @Inject constructor(
    @Named("menusRef") private val menusRef: CollectionReference,
    private val cartUseCase: CartUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _cartItems = MutableStateFlow<List<CartEntity>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val menuList: Flow<PagingData<Menu>> = combine(_searchQuery, _cartItems) { query, cartItems ->
        query to cartItems
    }.flatMapLatest { (query, cartItems) ->
        Pager(PagingConfig(pageSize = 10)) {
            MenuPagingSource(menusRef, cartItems, query)
        }.flow.cachedIn(viewModelScope)
    }

    init {
        fetchCart()
    }

    private fun fetchCart() {
        viewModelScope.launch {
            cartUseCase.getCart().collectLatest { items ->
                _cartItems.value = items
            }
        }
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            if (cart != null) {
                cartUseCase.insertMenu(cart.copy(menuQty = cart.menuQty + 1))
            } else {
                insertMenu(menuId)
            }
        }
    }

    fun decreaseOrderQuantity(cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                if (it.menuQty > 0) {
                    cartUseCase.insertMenu(it.copy(menuQty = it.menuQty - 1))
                }
            }
        }
    }

    fun getCartByMenuId(menuId: String, result: (CartEntity?) -> Unit) {
        viewModelScope.launch {
            val cartItem = _cartItems.value.find { it.menuId == menuId }
            result(cartItem)
        }
    }

    private fun insertMenu(menuId: String) {
        viewModelScope.launch {
            cartUseCase.insertMenu(CartEntity(menuId = menuId, menuQty = 1))
        }
    }
}