package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.remote.pushNotification.retrofit.FcmApi
import com.example.alfaresto_customersapp.utils.Constants.SERVER_IP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FcmApiModule {

    @Provides
    @Singleton
    fun getProvides(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SERVER_IP)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun getApi(retrofit: Retrofit): FcmApi {
        return retrofit.create(FcmApi::class.java)
    }
}