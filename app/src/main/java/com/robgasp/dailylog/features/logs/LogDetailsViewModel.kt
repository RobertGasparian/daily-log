package com.robgasp.dailylog.features.logs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.domain.GetDLogByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel(assistedFactory = LogDetailsViewModel.Factory::class)
class LogDetailsViewModel @AssistedInject constructor(
    @Assisted private val logId: String,
    private val getDLogByIdUC: GetDLogByIdUseCase,
) : BaseViewModel<LogDetailsViewModel.UIState, LogDetailsViewModel.Event>(UIState.initialState()) {

    init {
        viewModelScope.launch {
            getDLogByIdUC(logId)?.let {log ->
                with(log) {
                    update {
                        it.copy(
                            title = title,
                            description = description,
                            time = logTime.toString(),
                            date = logDate.toString(),
                            createdAt = creationDate.toString(),
                            modifiedAt = modificationDate.toString(),
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: String): LogDetailsViewModel
    }

    data class UIState(
        val title: String,
        val description: String?,
        val time: String,
        val date: String,
        val createdAt: String,
        val modifiedAt: String?,
    ) {
        companion object {
            fun initialState(): UIState {
                return UIState(
                    title = "",
                    description = "",
                    time = "",
                    date = "",
                    createdAt = "",
                    modifiedAt = null,
                )
            }
        }
    }

    sealed interface Event
}
