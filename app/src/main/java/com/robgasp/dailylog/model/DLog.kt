package com.robgasp.dailylog.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DLog(
    val id: String,
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime? = null,
    val title: String,
    val description: String? = null,
)
