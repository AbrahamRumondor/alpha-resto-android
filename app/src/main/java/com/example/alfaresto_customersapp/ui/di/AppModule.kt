package com.example.alfaresto_customersapp.ui.di

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
abstract class AppModule {

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
}