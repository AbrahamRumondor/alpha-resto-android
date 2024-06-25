package com.example.alfaresto_customersapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_db")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val menuId: String,
    val menuQty: Int
)