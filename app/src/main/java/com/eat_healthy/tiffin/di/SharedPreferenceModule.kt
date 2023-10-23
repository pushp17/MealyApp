package com.eat_healthy.tiffin.di

import android.content.Context
import com.eat_healthy.tiffin.utils.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferenceModule {
    @Provides
    @Singleton
    fun provideSharedPreferenceManager(@ApplicationContext context: Context):SharedPrefManager{
        return SharedPrefManager(context)
    }
}