package com.robgasp.dailylog.core

import com.robgasp.dailylog.core.provider.InstantProvider
import com.robgasp.dailylog.core.provider.DateTimeProvider
import com.robgasp.dailylog.core.provider.TimeZoneProvider
import jakarta.inject.Inject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class LogDateTimeProvider @Inject constructor(
    private val timeZoneProvider: TimeZoneProvider,
    private val clockProvider: InstantProvider,
) : DateTimeProvider {
    override fun currentDate(): LocalDate {
        return currentDateTime().date
    }

    override fun currentTime(): LocalTime {
        return currentDateTime().time
    }

    override fun currentDateTime(): LocalDateTime {
        val nowInstant = clockProvider.get()
        val currentZone = timeZoneProvider.get()
        val localDateTime = nowInstant.toLocalDateTime(currentZone)
        return localDateTime
    }
}
