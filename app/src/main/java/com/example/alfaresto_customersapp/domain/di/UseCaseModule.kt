package com.example.alfaresto_customersapp.domain.di

import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun provideCartUseCase(
        cartUseCaseImpl: CartUseCaseImpl
    ): CartUseCase

    abstract fun provideMenuUseCase(
        menuUseCaseImpl: MenuUseCaseImpl
    ): MenuUseCase
}