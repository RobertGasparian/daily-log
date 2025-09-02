package com.robgasp.dailylog.di

import com.robgasp.dailylog.core.LogDateTimeProvider
import com.robgasp.dailylog.util.DateTimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesDateProvider(logDateTimeProvider: LogDateTimeProvider): DateTimeProvider =
        logDateTimeProvider
}
