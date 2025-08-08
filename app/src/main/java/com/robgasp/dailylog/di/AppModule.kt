package com.robgasp.dailylog.di

import com.robgasp.dailylog.util.DateProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate
import java.time.LocalTime

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesDateProvider(): DateProvider {
        return object : DateProvider {
            override fun currentDate(): LocalDate {
                return LocalDate.now()
            }

            override fun currentTime(): LocalTime {
                return LocalTime.now()
            }
        }
    }
}
