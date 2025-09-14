package com.robgasp.dailylog.features.logs

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.domain.GetDLogsListUseCase
import com.robgasp.dailylog.domain.SeparateToDailyGroupsUseCase
import com.robgasp.dailylog.features.logs.LogsViewModel.InternalState.InternalStatus
import com.robgasp.dailylog.model.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

    private val internalState: MutableStateFlow<InternalState> =
        MutableStateFlow(InternalState.initialState())

    init {
        viewModelScope.launch {
            getLogsListUC().collect { logs ->
                internalState.update {
                    it.copy(
                        sections = separateToDailyGroupsUC(logs).toInternalSectionList(
                            prevCollapsedSections = it.getAllCollapsedDates()
                        ),
                        loadingStatus = InternalStatus.SUCCESS
                    )
                }
            }
        }
        viewModelScope.launch {
            internalState.collect { internalState ->
                update { uiState ->
                    uiState.copy(
                        sections = internalState.sections.toUIStateSectionList(
                            dateToGroupTitleMapper
                        ),
                        loadingStatus = internalState.loadingStatus.toUIStateStatus(),
                    )
                }
            }
        }
    }

    val intents: LogsScreenIntents = object : LogsScreenIntents {
        override fun onOpenDetailedLog(id: String) {
            post(Navigate(id))
        }

        override fun onToggleGroup(index: Int) {
            internalState.update {
                it.copy(
                    sections = it.sections.mapIndexed { i, section ->
                        if (index == i) section.copy(isCollapsed = !section.isCollapsed) else section
                    }
                )
            }
        }

        override fun onErrorDismiss() {
            update { it.copy(loadingStatus = UIState.Status.SUCCESS) }
        }
    }

    private fun InternalStatus.toUIStateStatus(): UIState.Status {
        return when (this) {
            InternalStatus.LOADING -> UIState.Status.LOADING
            InternalStatus.SUCCESS -> UIState.Status.SUCCESS
            InternalStatus.ERROR -> UIState.Status.ERROR
        }
    }

    private fun SortedMap<LocalDate, List<DLog>>.toInternalSectionList(
        prevCollapsedSections: Set<LocalDate> = emptySet(),
    ): List<InternalState.InternalSection> {
        return entries.toList().map { (date, logs) ->
            InternalState.InternalSection(
                date = date,
                logs = logs,
                isCollapsed = prevCollapsedSections.contains(date)
            )
        }
    }

    private fun List<InternalState.InternalSection>.toUIStateSectionList(
        dateToGroupTitleMapper: Mapper<LocalDate, String>,
    ): List<UIState.Section> {
        return this.map {
            UIState.Section(
                title = dateToGroupTitleMapper.mapTo(it.date),
                isCollapsed = it.isCollapsed,
                logs = it.logs.toUIStateLogList()
            )
        }
    }

    private fun List<DLog>.toUIStateLogList(): List<UIState.UILog> {
        return this.map {
            UIState.UILog(
                id = it.id,
                title = it.title,
                description = it.description,
                time = it.logTime.toString(), // TODO: Formal later on
                date = it.logDate.toString(), // TODO: Formal later on
            )
        }
    }


    internal data class InternalState(
        val sections: List<InternalSection>,
        val loadingStatus: InternalStatus,
    ) {
        companion object {
            fun initialState(): InternalState {
                return InternalState(
                    sections = emptyList(),
                    loadingStatus = InternalStatus.LOADING,
                )
            }
        }

        data class InternalSection(
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

        enum class InternalStatus {
            LOADING,
            SUCCESS,
            ERROR
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
            val isCollapsed: Boolean,
            val logs: List<UILog>,
        )

        data class UILog(
            val id: String,
            val title: String,
            val description: String?,
            val time: String,
            val date: String,
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
