package com.example.alfaresto_customersapp.data.di

import com.example.alfaresto_customersapp.data.remote.retrofit.FcmApi
import com.example.alfaresto_customersapp.data.remote.retrofit.OsrmApi
import com.example.alfaresto_customersapp.utils.singleton.Constants.SERVER_IP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    @Named("fcmRetrofit")
    fun getProvides(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SERVER_IP)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun getFcmApi(
        @Named("fcmRetrofit") retrofit: Retrofit
    ): FcmApi {
        return retrofit.create(FcmApi::class.java)
    }

    @Provides
    @Singleton
    @Named("osrmRetrofit")
    fun getOsrmProvides(): Retrofit {
       return Retrofit.Builder()
            .baseUrl("http://router.project-osrm.org") // Replace with your OSRM API base URL
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun getOsrmApi(@Named("osrmRetrofit")retrofit: Retrofit): OsrmApi {
        return retrofit.create(OsrmApi::class.java)
    }
}