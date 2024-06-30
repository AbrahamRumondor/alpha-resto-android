package com.example.alfaresto_customersapp.ui.components.restoTab

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.error.FirestoreCallback
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.ui.components.loadState.LoadStateViewModel
import com.example.alfaresto_customersapp.utils.user.UserConstants.USER_TOKEN
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
                val fetchedMenus = menuUseCase.getMenus().value
                _menus.value = fetchedMenus
                setLoading(false)
            } catch (e: Exception) {
                Log.e("MENU", "Error fetching menus: ${e.message}")
            }
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            try {
                cartUseCase.getCart().collect {
                    _cartCount.value = 0
                    it.map {
                        _cartCount.value += it.menuQty
                    }
                    _cart.value = it
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
                cartUseCase.insertMenu(it.copy(menuQty = it.menuQty + 1))
            } ?: insertMenu(menuId = menuId, menuQty = 1)
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
                Log.d("token", token)
                saveToken(token)
            }
        }.addOnFailureListener { e ->
            Log.e("token", "Failed to get token", e)
        }
    }

    private fun saveToken(token: String) {
        getUserFromDB(object : FirestoreCallback {
            override fun onSuccess(user: User?) {
                userUseCase.saveTokenToDB(user?.id ?: "", token)
            }

            override fun onFailure(exception: Exception) {
                Log.d("PushNotificationService", "Error adding FCM token to Firestore", exception)
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