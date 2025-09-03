package com.robgasp.dailylog.domain

import com.robgasp.dailylog.model.DLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDLogsListUseCase @Inject constructor(
    private val dLogRepo: DLogRepository,
) {
    operator fun invoke(): Flow<List<DLog>> = dLogRepo.getAllDLogsFlow()
}
