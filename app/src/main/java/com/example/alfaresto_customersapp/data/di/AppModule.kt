package com.example.alfaresto_customersapp.data.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("menusRef")
    fun provideMenusRef(): CollectionReference {
        return Firebase.firestore.collection("menus")
    }

    @Provides
    @Singleton
    @Named("ordersRef")
    fun provideOrdersRef(): CollectionReference {
        return Firebase.firestore.collection("orders")
    }

    @Provides
    @Singleton
    @Named("shipmentsRef")
    fun provideShipmentRef(): CollectionReference {
        return Firebase.firestore.collection("shipments")
    }
}
