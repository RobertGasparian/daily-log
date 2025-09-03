package com.robgasp.dailylog.features.logs.di

import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.features.logs.DateToGroupTitleMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.LocalDate

@Module
@InstallIn(SingletonComponent::class)
class LogsModule {
    @Provides
    fun providesDateToGroupTitleMapper(impl: DateToGroupTitleMapper): Mapper<LocalDate, String> =
        impl
}
