package com.example.alfaresto_customersapp.data.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.domain.model.Menu
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class MenuPagingSource(
    private val menusRef: CollectionReference
) : PagingSource<QuerySnapshot, Menu>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Menu> {
        return try {
            val currentPage = params.key ?: menusRef.get().addOnSuccessListener {
                Log.d("MenuPagingSource", "Success: $it")
                val menus = it.toObjects(MenuResponse::class.java)
                Log.d("MenuPagingSource", "Menus: $menus")
            }.await()

            val menus = currentPage.toObjects<MenuResponse>().map { MenuResponse.transform(it) }

            LoadResult.Page(
                data = menus,
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