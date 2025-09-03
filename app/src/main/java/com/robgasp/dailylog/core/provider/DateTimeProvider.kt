package com.robgasp.dailylog.core.provider

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

interface DateTimeProvider {
    fun currentDate(): LocalDate
    fun currentTime(): LocalTime
    fun currentDateTime(): LocalDateTime
}
