package com.robgasp.dailylog.data.di

import android.content.Context
import androidx.room.Room
import com.robgasp.dailylog.data.datasource.local.AppDataBase
import com.robgasp.dailylog.data.datasource.local.DLogRepositoryImpl
import com.robgasp.dailylog.data.datasource.local.LogDao
import com.robgasp.dailylog.domain.DLogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object LogModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDataBase =
        Room.databaseBuilder(ctx, AppDataBase::class.java, "app.db")
            .build()

    @Provides
    fun provideNoteDao(db: AppDataBase): LogDao = db.logDao()

    @Provides
    fun providesDLogRepository(repoImpl: DLogRepositoryImpl): DLogRepository = repoImpl
}
