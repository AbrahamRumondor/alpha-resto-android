package com.example.alfaresto_customersapp.ui.components.restoPage

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.callbacks.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import com.example.alfaresto_customersapp.utils.singleton.UserInfo.USER_TOKEN
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RestoViewModel @Inject constructor(
    private val menuUseCase: MenuUseCase,
    private val cartUseCase: CartUseCase,
    private val userUseCase: UserUseCase
) : LoadStateViewModel() {

    private val _menus: MutableStateFlow<List<Menu>> = MutableStateFlow(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    private val _cart: MutableStateFlow<List<CartEntity>> = MutableStateFlow(emptyList())
    val cart: StateFlow<List<CartEntity>> = _cart

    private val _cartCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount

    init {
        fetchMenus()
        fetchCart()
        getToken()
    }

    private fun fetchMenus() {
        viewModelScope.launch {
            setLoading(true)
            try {
                menuUseCase.getMenus().collectLatest {list->
                    _menus.value = list.sortedByDescending { it.dateCreated }.take(3)
                    setLoading(false)
                }
            } catch (e: Exception) {
                Timber.tag("MENU").e("Error fetching menus: %s", e.message)
            }
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                cartUseCase.getCart().collectLatest {
                    _cartCount.value = 0
                    it.map {
                        _cartCount.value += it.menuQty
                    }
                    _cart.value = it
                }
            } catch (e: Exception) {
                Timber.tag("CART").e("Error fetching cart: %s", e.message)
            }
        }
    }

    fun deleteCartByMenuId(menuId: String) {
        viewModelScope.launch {
            cartUseCase.deleteMenu(menuId = menuId)
        }
    }

    private fun insertMenu(menuId: String) {
        viewModelScope.launch {
            val cartEntity = CartEntity(menuId = menuId, menuQty = 1)
            cartUseCase.insertMenu(cartEntity)
        }
    }

    fun getCart(): Flow<List<CartEntity>> {
        return cartUseCase.getCart()
    }

    fun deleteAllCartItems() {
        viewModelScope.launch {
            cartUseCase.deleteAllMenus()
        }
    }

    fun addOrderQuantity(context: Context, menuId: String, cart: CartEntity?) {
        var isFromUserClick = menuId.isNotEmpty()
        Timber.tag("test").d("%s dan %s", cart?.menuId, cart?.menuQty)
        menuUseCase.getMenuStock(menuId) { stock ->
            if (isFromUserClick) {
                isFromUserClick = false
                if (stock == 0) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.out_of_stock),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@getMenuStock
                }
                cart?.let {
                    if (it.menuQty < stock) {
                        viewModelScope.launch {
                            cartUseCase.insertMenu(it.copy(menuQty = it.menuQty + 1))
                            return@launch
                        }
                        Log.d("orderss", "addOrderQuantity: ${it.menuQty}")
                    } else {
                        Log.d("orderss", "cartstock: ${it.menuQty} && stock: $stock")

                        Toast.makeText(
                            context,
                            context.getString(R.string.reach_max_stock),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } ?: insertMenu(menuId)
                return@getMenuStock
            }
        }
    }

    fun decreaseOrderQuantity(menuId: String, cart: CartEntity?) {
        viewModelScope.launch {
            cart?.let {
                if (cart.menuQty > 0) cartUseCase.insertMenu(it.copy(menuQty = cart.menuQty - 1))
                else cartUseCase.deleteMenu(it.menuId)
            }
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            if (token != null) {
                USER_TOKEN = token
                saveToken(token)
            }
        }.addOnFailureListener { e ->
            Timber.tag("token").e(e, "Failed to get token")
        }
    }

    private fun saveToken(token: String) {
        getUserFromDB(object : FirestoreCallback {
            override fun onSuccess(user: User?) {
                userUseCase.saveTokenToDB(user?.id ?: "", token)
            }

            override fun onFailure(exception: Exception) {
                Timber.tag("PushNotificationService")
                    .d(exception, "Error adding FCM token to Firestore")
            }
        })
    }

    fun getUserFromDB(callback: FirestoreCallback) {
        viewModelScope.launch {
            try {
                val user = userUseCase.getCurrentUser()
                callback.onSuccess(user.value)
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }
    }
}