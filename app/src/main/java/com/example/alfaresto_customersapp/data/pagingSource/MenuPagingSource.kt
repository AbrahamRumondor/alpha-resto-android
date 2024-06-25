package com.example.alfaresto_customersapp.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.domain.model.Menu
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class MenuPagingSource(
    private val menusRef: CollectionReference,
    private val cartItems: List<CartEntity>?
) : PagingSource<QuerySnapshot, Menu>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Menu> {
        return try {
            val currentPage = params.key ?: menusRef.get().addOnSuccessListener {
                it.toObjects(MenuResponse::class.java)
            }.await()

            val menus = currentPage.toObjects<MenuResponse>().map { MenuResponse.transform(it) }

            val updatedMenus = cartItems?.let {
                menus.map { menu ->
                    val cartItem = it.find { cart -> cart.menuId == menu.id }
                    if (cartItem != null) {
                        menu.copy(orderCartQuantity = cartItem.menuQty)
                    } else {
                        menu
                    }
                }
            } ?: menus

            LoadResult.Page(
                data = updatedMenus,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Menu>): QuerySnapshot? {
        return null
    }
}