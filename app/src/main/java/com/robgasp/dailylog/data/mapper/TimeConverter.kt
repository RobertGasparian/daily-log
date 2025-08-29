package com.robgasp.dailylog.data.mapper

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Suppress("unused")
object TimeConverter {

    @TypeConverter
    @JvmStatic
    fun ldtFromString(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.Companion.parse(it) } // ISO-8601 by default

    @TypeConverter
    @JvmStatic
    fun ldtToString(value: LocalDateTime?): String? =
        value?.toString()

    @TypeConverter
    @JvmStatic
    fun ldFromString(value: String?): LocalDate? =
        value?.let { LocalDate.Companion.parse(it) } // ISO-8601 by default

    @TypeConverter
    @JvmStatic
    fun ldtToString(value: LocalDate?): String? =
        value?.toString()

    @TypeConverter
    @JvmStatic
    fun ltFromString(value: String?): LocalTime? =
        value?.let { LocalTime.Companion.parse(it) } // ISO-8601 by default

    @TypeConverter
    @JvmStatic
    fun ltToString(value: LocalTime?): String? =
        value?.toString()
}
