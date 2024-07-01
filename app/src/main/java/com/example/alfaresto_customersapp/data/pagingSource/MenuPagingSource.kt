package com.example.alfaresto_customersapp.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.domain.model.Menu
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class MenuPagingSource(
    private val menusRef: CollectionReference,
    private val cartItems: List<CartEntity>,
    private val query: String?
) : PagingSource<QuerySnapshot, Menu>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Menu> {
        return try {
            val currentPage = params.key ?: menusRef.limit(params.loadSize.toLong()).get().await()

            val menus = currentPage.toObjects(MenuResponse::class.java)
                .map { MenuResponse.transform(it) }

            val filteredMenus = if (!query.isNullOrEmpty()) {
                menus.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                menus
            }

            val updatedMenus = filteredMenus.map { menu ->
                val cartItem = cartItems.find { it.menuId == menu.id }
                if (cartItem != null) {
                    menu.copy(orderCartQuantity = cartItem.menuQty)
                } else {
                    menu
                }
            }

            val lastVisible = currentPage.documents.lastOrNull()
            val nextPage = lastVisible?.let {
                menusRef.startAfter(it).limit(params.loadSize.toLong()).get().await()
            }

            LoadResult.Page(
                data = updatedMenus,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Menu>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}