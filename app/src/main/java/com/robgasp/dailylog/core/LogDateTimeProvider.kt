package com.robgasp.dailylog.core

import com.robgasp.dailylog.util.DateTimeProvider
import jakarta.inject.Inject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class LogDateTimeProvider @Inject constructor() : DateTimeProvider {
    override fun currentDate(): LocalDate {
        return currentDateTime().date
    }

    override fun currentTime(): LocalTime {
        return currentDateTime().time
    }

    override fun currentDateTime(): LocalDateTime {
        val nowInstant = Clock.System.now()
        val currentZone = TimeZone.currentSystemDefault()
        val localDateTime = nowInstant.toLocalDateTime(currentZone)
        return localDateTime
    }
}
