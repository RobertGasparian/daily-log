package com.robgasp.dailylog.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class DLog(
    val id: String,
    val title: String,
    val description: String? = null,
    val logTime: LocalTime,
    val logDate: LocalDate,
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime? = null,
)
