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
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListAllMenuViewModel @Inject constructor(
    @Named("menusRef") private val menusRef: CollectionReference
) : ViewModel() {

    val menuList: Flow<PagingData<Menu>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        MenuPagingSource(menusRef)
    }.flow.cachedIn(viewModelScope)
}
