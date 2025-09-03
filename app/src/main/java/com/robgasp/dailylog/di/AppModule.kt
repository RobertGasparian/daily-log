package com.robgasp.dailylog.di

import com.robgasp.dailylog.core.LogDateTimeProvider
import com.robgasp.dailylog.core.provider.DateTimeProvider
import com.robgasp.dailylog.core.provider.InstantProvider
import com.robgasp.dailylog.core.provider.LabelProvider
import com.robgasp.dailylog.core.provider.TimeZoneProvider
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

    @Provides
    fun providesTimeZoneProvider(): TimeZoneProvider = object : TimeZoneProvider {}

    @Provides
    fun providesInstantProvider(): InstantProvider = object : InstantProvider {}

    @Provides
    fun providesLabelProvider(): LabelProvider = object : LabelProvider {}
}
