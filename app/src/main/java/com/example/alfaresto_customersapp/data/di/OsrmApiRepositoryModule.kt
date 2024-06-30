package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.remote.osrm.OsrmApiRepositoryImpl
import com.example.alfaresto_customersapp.data.remote.pushNotification.FcmApiRepositoryImpl
import com.example.alfaresto_customersapp.domain.repository.FcmApiRepository
import com.example.alfaresto_customersapp.domain.repository.OsrmApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
abstract class OsrmApiRepositoryModule {
    @Binds
    abstract fun provideFcmApiRepository(
        osrmApiRepositoryImpl: OsrmApiRepositoryImpl
    ): OsrmApiRepository
}