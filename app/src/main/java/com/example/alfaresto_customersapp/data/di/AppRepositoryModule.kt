package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
abstract class AppRepositoryModule {

    @Binds
    abstract fun provideMenuRepository(
        menuRepositoryImpl: MenuRepositoryImpl
    ): MenuRepository
}