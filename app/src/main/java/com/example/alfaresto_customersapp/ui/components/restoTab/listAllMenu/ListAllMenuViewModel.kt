package com.example.alfaresto_customersapp.ui.components.restoTab.listAllMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alfaresto_customersapp.data.pagingSource.MenuPagingSource
import com.example.alfaresto_customersapp.domain.model.Menu
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ListAllMenuViewModel @Inject constructor(
    private val menusRef: CollectionReference
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

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }
}
