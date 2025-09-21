package com.robgasp.dailylog.features.logs.di

import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.util.DateToGroupTitleMapper
import com.robgasp.dailylog.util.DateToTextMapper
import com.robgasp.dailylog.util.TimeToTextMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class LogsModule {
    @Provides
    @Named(GROUP_TITLE_MAPPER)
    fun providesDateToGroupTitleMapper(impl: DateToGroupTitleMapper): Mapper<LocalDate, String> =
        impl

    @Provides
    fun providesDateToTextMapper(impl: DateToTextMapper): Mapper<LocalDate, String> =
        impl

    @Provides
    fun providesTimeToTextMapper(impl: TimeToTextMapper): Mapper<LocalTime, String> =
        impl

    companion object {
        const val GROUP_TITLE_MAPPER = "groupTitleMapper"
    }
}
