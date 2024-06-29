package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.local.room.repository.CartRepositoryImpl
import com.example.alfaresto_customersapp.domain.repository.CartRepository
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.example.alfaresto_customersapp.domain.usecase.menuDetail.MenuDetailUseCase
import com.example.alfaresto_customersapp.domain.usecase.menuDetail.MenuDetailUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppRepositoryModule {

    @Binds
    abstract fun provideMenuRepository(
        menuRepositoryImpl: MenuRepositoryImpl
    ): MenuRepository

    @Binds
    abstract fun provideCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun provideMenuDetailUseCase(
        menuDetailUseCaseImpl: MenuDetailUseCaseImpl
    ): MenuDetailUseCase
}