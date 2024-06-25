package com.example.alfaresto_customersapp.ui.di

import com.example.alfaresto_customersapp.domain.usecase.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.MenuUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.shipment.ShipmentUseCase
import com.example.alfaresto_customersapp.domain.usecase.shipment.ShipmentUseCaseImpl
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCaseImpl
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
    abstract fun provideOrderUseCase(
        orderUseCaseImpl: OrderUseCaseImpl
    ): OrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideShipmentUseCase(
        shipmentUseCaseImpl: ShipmentUseCaseImpl
    ): ShipmentUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideOrderHistoryUseCase(
        orderHistoryUseCaseImpl: OrderHistoryUseCaseImpl
    ): OrderHistoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUserUseCase(
        userUseCaseImpl: UserUseCaseImpl
    ): UserUseCase

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

    @Binds
    @ViewModelScoped
    abstract fun provideCartUseCase(
        cartUseCaseImpl: CartUseCaseImpl
    ): CartUseCase
}