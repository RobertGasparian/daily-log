package com.robgasp.dailylog.features.logs

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.domain.GetDLogsListUseCase
import com.robgasp.dailylog.model.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

//TODO: error handling is absent for now
@HiltViewModel
class LogsViewModel @Inject constructor(
    private val getLogsListUC: GetDLogsListUseCase,
) : BaseViewModel<LogsViewModel.UIState, LogsViewModel.Event>(UIState.initialState()) {

    init {
        viewModelScope.launch {
            getLogsListUC().collect { logs ->
                update { it.copy(logs = logs, loadingStatus = UIState.Status.SUCCESS) }
            }
        }
    }

    fun openDetailedLog(log: DLog) {
        post(Navigate(log.id))
    }

    fun errorDismiss() {
        update { it.copy(loadingStatus = UIState.Status.SUCCESS) }
    }

    data class UIState(
        val logs: List<DLog>,
        val loadingStatus: Status,
    ) {
        companion object {
            fun initialState(): UIState {
                return UIState(
                    logs = emptyList(),
                    loadingStatus = Status.LOADING,
                )
            }
        }

        enum class Status {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Event
    data class Navigate(val logId: String) : Event
}
