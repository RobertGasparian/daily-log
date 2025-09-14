package com.robgasp.dailylog.features.create

import com.robgasp.dailylog.core.provider.DateTimeProvider
import com.robgasp.dailylog.model.DLog
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal fun CreateViewModel.UIState.toDLog(dateTimeProvider: DateTimeProvider): DLog {
    val id = Uuid.Companion.random().toString()
    return DLog(
        id = id,
        title = this.title,
        description = this.description,
        logTime = this.time,
        logDate = this.day,
        creationDate = dateTimeProvider.currentDateTime(),
        modificationDate = null
    )
}
