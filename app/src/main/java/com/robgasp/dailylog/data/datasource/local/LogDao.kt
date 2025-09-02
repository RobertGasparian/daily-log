package com.robgasp.dailylog.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.robgasp.dailylog.data.model.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Query(
        """
        SELECT * FROM logs
        ORDER BY day DESC, time DESC, id DESC
    """
    )
    fun observeAllLogs(): Flow<List<LogEntity>>

    @Query(
        """
        SELECT * FROM logs
        ORDER BY day DESC, time DESC, id DESC
    """
    )
    suspend fun getAllLogs(): List<LogEntity>

    @Query("SELECT * FROM logs WHERE id = :id")
    fun observeLogById(id: String): Flow<LogEntity?>

    @Query("SELECT * FROM logs WHERE id = :id")
    suspend fun getLogById(id: String): LogEntity?

    @Upsert
    suspend fun upsert(log: LogEntity)

    @Delete
    suspend fun delete(log: LogEntity): Int

    @Query("DELETE FROM logs WHERE id = :id")
    suspend fun deleteById(id: String): Int
}
