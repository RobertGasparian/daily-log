package com.robgasp.dailylog.features.logs

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.domain.GetDLogsListUseCase
import com.robgasp.dailylog.domain.SeparateToDailyGroupsUseCase
import com.robgasp.dailylog.model.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.util.SortedMap

// TODO: error handling is absent for now
@HiltViewModel
class LogsViewModel @Inject constructor(
    private val getLogsListUC: GetDLogsListUseCase,
    private val separateToDailyGroupsUC: SeparateToDailyGroupsUseCase,
    private val dateToGroupTitleMapper: Mapper<LocalDate, String>,
) : BaseViewModel<LogsViewModel.UIState, LogsViewModel.Event>(UIState.initialState()) {

    init {
        viewModelScope.launch {
            getLogsListUC().collect { logs ->
                update {
                    it.copy(
                        logs = separateToDailyGroupsUC(logs).toSectionList(dateToGroupTitleMapper),
                        loadingStatus = UIState.Status.SUCCESS
                    )
                }
            }
        }
    }

    private fun SortedMap<LocalDate, List<DLog>>.toSectionList(dateToGroupTitleMapper: Mapper<LocalDate, String>): List<UIState.Section> {
        return entries.toList().map { (date, logs) ->
            UIState.Section(
                title = dateToGroupTitleMapper.mapTo(date),
                date = date,
                logs = logs
            )
        }
    }

    fun openDetailedLog(log: DLog) {
        post(Navigate(log.id))
    }

    fun errorDismiss() {
        update { it.copy(loadingStatus = UIState.Status.SUCCESS) }
    }

    data class UIState(
        val logs: List<Section>,
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

        data class Section(
            val title: String,
            val date: LocalDate,
            val logs: List<DLog>,
        )

        enum class Status {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Event
    data class Navigate(val logId: String) : Event
}
