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
                        sections = separateToDailyGroupsUC(logs).toSectionList(
                            dateToGroupTitleMapper = dateToGroupTitleMapper,
                            prevCollapsedSections = it.getAllCollapsedDates()
                        ),
                        loadingStatus = UIState.Status.SUCCESS
                    )
                }
            }
        }
    }

    private fun SortedMap<LocalDate, List<DLog>>.toSectionList(
        dateToGroupTitleMapper: Mapper<LocalDate, String>,
        prevCollapsedSections: Set<LocalDate> = emptySet(),
    ): List<UIState.Section> {
        return entries.toList().map { (date, logs) ->
            UIState.Section(
                title = dateToGroupTitleMapper.mapTo(date),
                date = date,
                logs = logs,
                isCollapsed = prevCollapsedSections.contains(date)
            )
        }
    }

    fun openDetailedLog(log: DLog) {
        post(Navigate(log.id))
    }

    fun errorDismiss() {
        update { it.copy(loadingStatus = UIState.Status.SUCCESS) }
    }

    fun toggleGroup(date: LocalDate) {
        update {
            val newSections = it.sections.map { section ->
                if (section.date == date) {
                    section.copy(isCollapsed = !section.isCollapsed)
                } else {
                    section
                }
            }
            it.copy(sections = newSections)
        }
    }

    data class UIState(
        val sections: List<Section>,
        val loadingStatus: Status,
    ) {
        companion object {
            fun initialState(): UIState {
                return UIState(
                    sections = emptyList(),
                    loadingStatus = Status.LOADING,
                )
            }
        }

        data class Section(
            val title: String,
            val date: LocalDate,
            val isCollapsed: Boolean,
            val logs: List<DLog>,
        )

        fun getAllCollapsedDates(): Set<LocalDate> {
            return sections.asSequence()
                .filter { it.isCollapsed }
                .map { it.date }
                .toSet()
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
