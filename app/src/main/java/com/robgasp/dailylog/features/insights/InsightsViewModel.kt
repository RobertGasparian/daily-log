package com.robgasp.dailylog.features.insights

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import timber.log.Timber

@HiltViewModel
class InsightsViewModel @Inject constructor() : ViewModel() {

    init {
        Timber.tag("vm_checker").d("init: $this")
    }

    override fun onCleared() {
        Timber.tag("vm_checker").d("onCleared: $this")
        super.onCleared()
    }
}
