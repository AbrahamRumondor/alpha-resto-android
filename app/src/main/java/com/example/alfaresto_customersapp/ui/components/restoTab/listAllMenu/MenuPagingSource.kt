package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alfaresto_customersapp.domain.dummy.MenuDummy
import com.example.alfaresto_customersapp.domain.model.Menu

class MenuPagingSource : PagingSource<Int, Menu>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Menu> {
        return try {
            val nextPageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val startIndex = (nextPageNumber - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, MenuDummy.menuList.size)

            val response = MenuDummy.menuList.subList(startIndex, endIndex)

            LoadResult.Page(
                data = response,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (endIndex < MenuDummy.menuList.size) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Menu>): Int? {
        return null
    }
}