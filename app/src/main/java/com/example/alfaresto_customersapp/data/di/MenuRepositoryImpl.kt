package com.example.alfaresto_customersapp.data.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menusRef: CollectionReference
) : MenuRepository {

    private val _menus = MutableLiveData<List<Menu>>(emptyList())
    private val menus: LiveData<List<Menu>> = _menus

    override suspend fun getMenus(): LiveData<List<Menu>> {
        try {
            val snapshot = menusRef.get().await()
            val menuList = snapshot.toObjects(MenuResponse::class.java)
            _menus.value = menuList.map { MenuResponse.transform(it) }
        } catch (e: Exception) {
            _menus.value = emptyList()
        }
        return menus
    }

    override suspend fun getMenuDetail(menuId: String): Menu? {
        return try {
            val document = menusRef.document(menuId).get().await()
            document.toObject(MenuResponse::class.java)?.let { MenuResponse.transform(it) }
        } catch (e: Exception) {
            null
        }
    }
}