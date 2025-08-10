package com.robgasp.dailylog.domain

import com.robgasp.dailylog.model.DLog
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GetDLogByIdUseCase {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(id: String): DLog {
        return DLog(
            id = "some_id",
            creationDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            modificationDate = null,
            title = "Title 1",

            description = "Some description bla bla blaaaaaaaaaaaa.....",
        )
    }
}
