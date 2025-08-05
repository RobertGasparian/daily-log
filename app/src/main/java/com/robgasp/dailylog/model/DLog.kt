package com.robgasp.dailylog.model

import java.time.LocalDateTime

data class DLog(
    val id: String,
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime? = null,
    val title: String,
    val description: String? = null,
)