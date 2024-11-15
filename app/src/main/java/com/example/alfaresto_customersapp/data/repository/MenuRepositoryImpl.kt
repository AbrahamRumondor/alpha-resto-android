package com.example.alfaresto_customersapp.data.repository

import android.util.Log
import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.data.model.OrderResponse
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.model.Order
import com.example.alfaresto_customersapp.domain.repository.CartRepository
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class MenuRepositoryImpl @Inject constructor(
    @Named("menusRef") private val menusRef: CollectionReference
) : MenuRepository {

    private val _menus = MutableStateFlow<List<Menu>>(emptyList())
    private val menus: StateFlow<List<Menu>> = _menus

    override suspend fun getMenus(): StateFlow<List<Menu>> {
        menusRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                _menus.value = emptyList()
                return@addSnapshotListener
            }

            val menuList = mutableListOf<Menu>()
            if (snapshots != null) {
                for (doc in snapshots) {
                    if (doc.exists()) {
                        val menuResponse = doc.toObject(MenuResponse::class.java)
                        val menu = MenuResponse.transform(menuResponse)
                        menuList.add(menu)
                    }
                }
            }
            _menus.value = menuList
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

    override suspend fun getNewMenus(): StateFlow<List<Menu>> {
        try {
            val snapshot = menusRef.get().await()
            val menuList = snapshot.toObjects(MenuResponse::class.java)
            val sortedMenu = menuList.map { MenuResponse.transform(it) }
                .sortedByDescending { it.dateCreated }.take(3)

            _menus.value = sortedMenu
        } catch (e: Exception) {
            _menus.value = emptyList()
        }
        return menus
    }

    override fun getMenuStock(menuId: String, onResult: (Int) -> Unit) {
        menusRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("getMenuStock", "Error fetching menu document: $error")
                onResult(0)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val menuResponse = snapshot.toObjects(MenuResponse::class.java)
                val stock = menuResponse.first { it.id == menuId }.stock
                onResult(stock)
                Log.d("getMenuStock", "Stock menu for $menuId: $stock")
            } else {
                onResult(0)
                Log.d("getMenuStock", "Menu document for $menuId doesn't exist")
            }
        }
    }

    override suspend fun updateMenuStock(menuId: String, stock: Int) {
        try {
            menusRef.document(menuId).update("menu_stock", stock).await()
            Timber.tag("updateMenuStock").d("Stock for $menuId updated to $stock")
        } catch (e: Exception) {
            Timber.tag("updateMenuStock").e("Error updating stock for $menuId: $e")
        }
    }
}