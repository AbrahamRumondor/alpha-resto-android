package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.domain.model.Response
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.example.alfaresto_customersapp.domain.repository.Menus
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menusRef: CollectionReference
) : MenuRepository {

    override fun getMenus(): Flow<Menus> = callbackFlow {
        val snapshotListener = menusRef.addSnapshotListener { snapshot, e ->
            val menusResponse = if (snapshot != null) {
                val menus = snapshot.toObjects(MenuResponse::class.java)
                val menuList = menus.map {
                    MenuResponse.transform(it)
                }
                Response.Success(menuList)
            } else {
                Response.Failure(e)
            }
            trySend(menusResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}