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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListAllMenuViewModel @Inject constructor(
    @Named("menusRef") private val menusRef: CollectionReference,
    private val cartUseCase: CartUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val searchQuery: StateFlow<String?> get() = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val menuList: Flow<PagingData<Menu>> = searchQuery.flatMapLatest { query ->
        Pager(
            PagingConfig(pageSize = 10)
        ) {
            MenuPagingSource(menusRef, query)
        }.flow.cachedIn(viewModelScope)
    }

    private val _menuList = MutableStateFlow<PagingData<Menu>>(PagingData.empty())
//    val menuList: StateFlow<PagingData<Menu>> get() = _menuList

    init {
        fetchCart()
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                cartUseCase.getCart().collectLatest { cartItems ->
                    val updatedPagingSource = MenuPagingSource(menusRef, cartItems)
                    val newPager = Pager(
                        PagingConfig(pageSize = 10)
                    ) {
                        updatedPagingSource
                    }.flow.cachedIn(viewModelScope)

                    newPager.collectLatest {
                        _menuList.value = it
                    }
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

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            Log.d("test", "${cart?.menuId} dan ${cart?.menuQty}")
            if (cart != null) {
                cartUseCase.insertMenu(cart.copy(menuQty = cart.menuQty + 1))
            } else {
                insertMenu(menuId = menuId, menuQty = 1)
            }
        }
    }

    fun decreaseOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                if (cart.menuQty > 0) cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty - 1))
            }
        }
    }

    fun getCartByMenuId(menuId: String, result: (CartEntity?) -> Unit) {
        viewModelScope.launch {
            cartUseCase.getCart().firstOrNull()?.map {
                if (it.menuId == menuId) {
                    result(it)
                    return@launch
                }
            }
            result(null)
        }
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }
}
