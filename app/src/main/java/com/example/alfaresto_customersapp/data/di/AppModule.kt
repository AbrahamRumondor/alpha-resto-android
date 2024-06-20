package com.example.alfaresto_customersapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.alfaresto_customersapp.data.local.room.CartDao
import com.example.alfaresto_customersapp.data.local.room.CartDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMenusRef(): CollectionReference {
        return Firebase.firestore.collection("menus")
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "cart_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: CartDatabase): CartDao {
        return database.cartDao()
    }
}