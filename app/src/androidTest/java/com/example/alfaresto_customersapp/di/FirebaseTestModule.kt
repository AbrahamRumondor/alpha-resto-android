package com.example.alfaresto_customersapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.google.firebase.FirebaseApp
import javax.inject.Singleton
import org.mockito.Mockito

@Module
@InstallIn(SingletonComponent::class) // Specifies that this module lives in the SingletonComponent
object FirebaseTestModule {

  @Provides
  @Singleton
  fun provideFirebaseApp(@ApplicationContext context: Context): FirebaseApp {
    // Return a mocked FirebaseApp instance
    return Mockito.mock(FirebaseApp::class.java)
  }
}