package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.repository.FcmApiRepositoryImpl
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
abstract class FcmApiRepositoryModule {
    @Binds
    abstract fun provideFcmApiRepository(
        fcmApiRepositoryImpl: FcmApiRepositoryImpl
    ): FcmApiRepository
}