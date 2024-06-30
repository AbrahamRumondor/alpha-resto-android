package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import android.util.Log
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
) : LoadStateViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val searchQuery: StateFlow<String?> get() = _searchQuery

    private val _menuList = MutableStateFlow<PagingData<Menu>>(PagingData.empty())

    @OptIn(ExperimentalCoroutinesApi::class)
    val menuList: Flow<PagingData<Menu>> = searchQuery.flatMapLatest { query ->
        fetchMenus(query)
    }.cachedIn(viewModelScope)

    init {
        fetchCart()
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                setLoading(true)
                cartUseCase.getCart().collectLatest { cartItems ->
                    _menuList.value = fetchMenus(null, cartItems).first()
                }
            } catch (e: Exception) {
                Log.e("CART", "Error fetching cart: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchMenus(
        query: String?,
        cartItems: List<CartEntity> = emptyList()
    ): Flow<PagingData<Menu>> {
        return Pager(
            PagingConfig(pageSize = 10)
        ) {
            MenuPagingSource(menusRef, cartItems, query)
        }.flow
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun insertMenu(menuId: String, menuQty: Int) {
        viewModelScope.launch {
            val cartEntity = CartEntity(menuId = menuId, menuQty = menuQty)
            cartUseCase.insertMenu(cartEntity)
        }
    }

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                cartUseCase.insertMenu(it.copy(menuQty = it.menuQty + 1))
            } ?: insertMenu(menuId = menuId, menuQty = 1)
        }
    }

    fun decreaseOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                if (cart.menuQty > 0) {
                    cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty - 1))
                }
            }
        }
    }

    fun getCartByMenuId(menuId: String, result: (CartEntity?) -> Unit) {
        viewModelScope.launch {
            val cart = cartUseCase.getCart().firstOrNull()?.find { it.menuId == menuId }
            result(cart)
        }
    }
}