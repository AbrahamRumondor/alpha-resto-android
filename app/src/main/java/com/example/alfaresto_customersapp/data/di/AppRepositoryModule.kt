package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.repository.CartRepositoryImpl
import com.example.alfaresto_customersapp.data.repository.AuthRepositoryImpl
import com.example.alfaresto_customersapp.data.repository.MenuRepositoryImpl
import com.example.alfaresto_customersapp.data.repository.OrderRepositoryImpl
import com.example.alfaresto_customersapp.data.repository.RestaurantRepositoryImpl
import com.example.alfaresto_customersapp.data.repository.ShipmentRepositoryImpl
import com.example.alfaresto_customersapp.data.repository.UserRepositoryImpl
import com.example.alfaresto_customersapp.domain.repository.AuthRepository
import com.example.alfaresto_customersapp.domain.repository.CartRepository
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.example.alfaresto_customersapp.domain.repository.OrderRepository
import com.example.alfaresto_customersapp.domain.repository.RestaurantRepository
import com.example.alfaresto_customersapp.domain.repository.ShipmentRepository
import com.example.alfaresto_customersapp.domain.repository.UserRepository
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
    abstract fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun provideCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun provideMenuDetailUseCase(
        menuDetailUseCaseImpl: MenuDetailUseCaseImpl
    ): MenuDetailUseCase

    @Binds
    abstract fun provideOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    abstract fun provideShipmentRepository(
        shipmentRepositoryImpl: ShipmentRepositoryImpl
    ): ShipmentRepository

    @Binds
    abstract fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun provideRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository
}