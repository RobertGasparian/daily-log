package com.robgasp.dailylog

import androidx.lifecycle.ViewModel
import com.robgasp.dailylog.domain.GetDLogByIdUseCase
import com.robgasp.dailylog.model.DLog
import com.robgasp.dailylog.util.DateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class LogEditorViewModel @Inject constructor(
    private val dateProvider: DateProvider,
    private val getDLogByIdUseCase: GetDLogByIdUseCase,
) : ViewModel() {

    init {
        Timber.tag("date_check").d( dateProvider.currentDate().toString())
    }

    fun getTime(): LocalTime {
        return dateProvider.currentTime()
    }

    fun getLog(id: String): DLog = getDLogByIdUseCase(id)
}
