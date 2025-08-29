package com.robgasp.dailylog.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robgasp.dailylog.data.mapper.TimeConverter
import com.robgasp.dailylog.data.model.LogEntity

@Database(
    entities = [
        LogEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(TimeConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun logDao(): LogDao
}
