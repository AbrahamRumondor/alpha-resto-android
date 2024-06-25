package com.example.alfaresto_customersapp.ui.di

import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCaseImpl
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
    abstract fun provideMenuUseCase(
        menuUseCaseImpl: MenuUseCaseImpl
    ): MenuUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(
        authUseCaseImpl: AuthUseCaseImpl
    ): AuthUseCase
}