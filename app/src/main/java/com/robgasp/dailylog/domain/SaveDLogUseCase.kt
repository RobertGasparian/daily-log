package com.robgasp.dailylog.domain

import com.robgasp.dailylog.model.DLog
import javax.inject.Inject

class SaveDLogUseCase @Inject constructor(
    private val dLogRepo: DLogRepository,
) {
    suspend operator fun invoke(dLog: DLog) = dLogRepo.addDLog(dLog)
}
