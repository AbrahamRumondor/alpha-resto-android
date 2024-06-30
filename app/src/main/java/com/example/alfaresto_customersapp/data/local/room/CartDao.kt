package com.example.alfaresto_customersapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert
    suspend fun insertMenu(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_db")
    fun getCart(): Flow<List<CartEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM cart_db WHERE menuId = :menuId)")
    fun getMenuById(menuId: String): Boolean

    @Query("DELETE FROM cart_db WHERE menuId = :menuId")
    fun deleteMenu(menuId: String)

    @Query("DELETE FROM cart_db")
    suspend fun deleteAllMenus()
}