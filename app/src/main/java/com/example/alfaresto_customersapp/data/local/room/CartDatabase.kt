package com.example.alfaresto_customersapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}