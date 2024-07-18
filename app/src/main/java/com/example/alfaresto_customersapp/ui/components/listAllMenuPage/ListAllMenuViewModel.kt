package com.example.alfaresto_customersapp.ui.components.listAllMenuPage

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.pagingSource.MenuPagingSource
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListAllMenuViewModel @Inject constructor(
    @Named("menusRef") private val menusRef: CollectionReference,
    private val cartUseCase: CartUseCase,
    private val menuUseCase: MenuUseCase
) : LoadStateViewModel() {

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

    private val _cartCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount

    init {
        fetchCart()
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                setLoading(true)
                cartUseCase.getCart().collectLatest { items ->
                    _cartCount.value = 0
                    items.map {
                        _cartCount.value += it.menuQty
                    }

                    _cartItems.value = items
                }
            } catch (e: Exception) {
                Timber.tag("CART").e("Error fetching cart: %s", e.message)
            }
        }
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun addOrderQuantity(menuId: String, cart: CartEntity?) {
        var isFromUserClick = menuId.isNotEmpty()

        menuUseCase.getMenuStock(menuId) { stock ->
            if (isFromUserClick) {
                isFromUserClick = false
                if (stock == 0) return@getMenuStock
                cart?.let {
                    if (it.menuQty < stock) {
                        viewModelScope.launch {
                            cartUseCase.insertMenu(it.copy(menuQty = it.menuQty + 1))
                            return@launch
                        }
                    }
                } ?: insertMenu(menuId)
                return@getMenuStock
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