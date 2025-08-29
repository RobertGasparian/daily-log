package com.robgasp.dailylog.data.mapper

import com.robgasp.dailylog.data.model.LogEntity
import com.robgasp.dailylog.model.DLog

fun LogEntity.toDLog(): DLog = DLog(
    id = id,
    title = title,
    description = description,
    logTime = time,
    logDate = day,
    creationDate = creationDate,
    modificationDate = modificationDate,
)

fun DLog.toLogEntity(): LogEntity = LogEntity(
    id = id,
    title = title,
    description = description,
    day = logDate,
    time = logTime,
    creationDate = creationDate,
    modificationDate = modificationDate,
)

fun List<DLog>.toLogEntityList(): List<LogEntity> {
    return this.map { it.toLogEntity() }
}

fun List<LogEntity>.toDLogList(): List<DLog> {
    return this.map { it.toDLog() }
}

