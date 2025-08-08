package com.robgasp.dailylog.domain

import com.robgasp.dailylog.model.DLog
import java.time.LocalDateTime

class GetDLogByIdUseCase {
    operator fun invoke(id: String): DLog {
        return DLog(
            id = "some_id",
            creationDate =     LocalDateTime.now(),
            modificationDate =      null,
            title = "Title 1",

            description = "Some description bla bla blaaaaaaaaaaaa.....",
        )
    }
}
