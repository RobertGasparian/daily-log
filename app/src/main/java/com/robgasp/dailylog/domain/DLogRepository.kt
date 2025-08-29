package com.robgasp.dailylog.domain

import com.robgasp.dailylog.model.DLog
import kotlinx.coroutines.flow.Flow

interface DLogRepository {
    fun getAllDLogsFlow(): Flow<List<DLog>>

    suspend fun getAllDLogsList(): List<DLog>

    fun getDLogFlowById(id: String): Flow<DLog?>

    suspend fun getDLogById(id: String): DLog?

    suspend fun addDLog(dLog: DLog)

    suspend fun deleteDLogById(id: String): Boolean

    suspend fun deleteDLog(dLog: DLog): Boolean
}
