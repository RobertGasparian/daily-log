package com.robgasp.dailylog.features.create

import androidx.lifecycle.ViewModel
import com.robgasp.dailylog.domain.GetDLogByIdUseCase
import com.robgasp.dailylog.domain.SaveDLogUseCase
import com.robgasp.dailylog.model.DLog
import com.robgasp.dailylog.util.DateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val dateProvider: DateProvider,
    private val getDLogByIdUseCase: GetDLogByIdUseCase,
    private val saveDLogUseCase: SaveDLogUseCase,
) : ViewModel() {
    init {
        Timber.Forest.tag("vm_checker").d("init: $this")
    }

    override fun onCleared() {
        Timber.Forest.tag("vm_checker").d("onCleared: $this")
        super.onCleared()
    }

    fun getTime(): LocalTime {
        return dateProvider.currentTime()
    }

    suspend fun getLog(id: String): DLog? = getDLogByIdUseCase(id)
    fun addLog() {
        TODO("Not yet implemented")
    }
}
