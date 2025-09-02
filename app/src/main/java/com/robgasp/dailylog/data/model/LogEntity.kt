package com.robgasp.dailylog.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey val id: String,
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime? = null,
    val time: LocalTime,
    val day: LocalDate,
    val title: String,
    val description: String? = null,
)
