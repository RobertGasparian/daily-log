package com.robgasp.dailylog.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "logs")
@OptIn(ExperimentalUuidApi::class)
data class LogEntity constructor(
    @PrimaryKey val id: String = Uuid.Companion.random().toString(),
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime? = null,
    val time: LocalTime,
    val day: LocalDate,
    val title: String,
    val description: String? = null,
)
