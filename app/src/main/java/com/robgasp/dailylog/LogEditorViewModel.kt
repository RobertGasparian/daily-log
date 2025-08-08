package com.robgasp.dailylog

import android.util.Log
import androidx.lifecycle.ViewModel
import com.robgasp.dailylog.util.DateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LogEditorViewModel @Inject constructor(
    private val dateProvider: DateProvider
) : ViewModel() {

    init {
        Log.d("date_check", dateProvider.currentDate().toString())
    }

    fun getTime(): LocalTime {
        return dateProvider.currentTime()
    }

    fun onTitleChange(title: String) {
    }

    fun onDescriptionChange(description: String) {
    }

    fun onDateChange(date: Date) {
    }
}