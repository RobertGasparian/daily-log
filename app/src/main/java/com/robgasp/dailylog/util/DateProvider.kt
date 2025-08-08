package com.robgasp.dailylog.util

import java.time.LocalDate
import java.time.LocalTime

interface DateProvider {
    fun currentDate(): LocalDate
    fun currentTime(): LocalTime
}
