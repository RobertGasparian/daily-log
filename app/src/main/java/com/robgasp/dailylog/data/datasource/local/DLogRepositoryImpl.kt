package com.robgasp.dailylog.data.datasource.local

import com.robgasp.dailylog.data.mapper.toDLog
import com.robgasp.dailylog.data.mapper.toDLogList
import com.robgasp.dailylog.data.mapper.toLogEntity
import com.robgasp.dailylog.domain.DLogRepository
import com.robgasp.dailylog.model.DLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DLogRepositoryImpl @Inject constructor(
    private val logDao: LogDao,
) : DLogRepository {

    override fun getAllDLogsFlow(): Flow<List<DLog>> {
        return logDao.observeAllLogs().map { it.toDLogList() }
    }

    override suspend fun getAllDLogsList(): List<DLog> {
        return logDao.getAllLogs().toDLogList()
    }

    override suspend fun getDLogById(id: String): DLog? {
        return logDao.getLogById(id)?.toDLog()
    }

    override fun getDLogFlowById(id: String): Flow<DLog?> {
        return logDao.observeLogById(id).map { it?.toDLog() }
    }

    override suspend fun addDLog(dLog: DLog) {
        logDao.upsert(dLog.toLogEntity())
    }

    override suspend fun deleteDLogById(id: String): Boolean {
        return logDao.deleteById(id) > 0
    }

    override suspend fun deleteDLog(dLog: DLog): Boolean {
        return logDao.delete(dLog.toLogEntity()) > 0
    }
}
