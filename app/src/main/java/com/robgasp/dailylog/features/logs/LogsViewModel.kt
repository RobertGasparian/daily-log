package com.robgasp.dailylog.features.logs

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.domain.GetDLogsListUseCase
import com.robgasp.dailylog.domain.SeparateToDailyGroupsUseCase
import com.robgasp.dailylog.features.logs.LogsViewModel.ModelState.InternalStatus
import com.robgasp.dailylog.model.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

// TODO: error handling is absent for now
@HiltViewModel
class LogsViewModel @Inject constructor(
    private val getLogsListUC: GetDLogsListUseCase,
    private val separateToDailyGroupsUC: SeparateToDailyGroupsUseCase,
    private val dateToGroupTitleMapper: Mapper<LocalDate, String>,
) : BaseViewModel<LogsViewModel.UIState, LogsViewModel.Event, LogsViewModel.Action, LogsScreenIntents>(
    UIState.initialState()
) {

    private val modelState: MutableStateFlow<ModelState> =
        MutableStateFlow(ModelState.initialState())

    init {
        loadData()
        convertModelToUIState()
    }

    override val intents: LogsScreenIntents = object : LogsScreenIntents {
        override fun onOpenDetailedLog(id: String) = fire(Action.OpenDetailedLog(id))
        override fun onToggleGroup(index: Int) = fire(Action.ToggleGroup(index))
        override fun onErrorDismiss() = fire(Action.ErrorDismiss)
    }

    sealed interface Action {
        data class OpenDetailedLog(val id: String) : Action
        data class ToggleGroup(val index: Int) : Action
        data object ErrorDismiss : Action
    }

    override fun reduce(action: Action) {
        when (action) {
            Action.ErrorDismiss -> {
                update { it.copy(loadingStatus = UIState.Status.SUCCESS) }
            }

            is Action.OpenDetailedLog -> {
                post(Navigate(action.id))
            }

            is Action.ToggleGroup -> {
                modelState.update {
                    it.copy(
                        sections = it.sections.mapIndexed { i, section ->
                            if (action.index == i) section.copy(isCollapsed = !section.isCollapsed) else section
                        }
                    )
                }
            }
        }
    }

    private fun loadData() {
        getLogsListUC()
            .onEach { logs ->
                modelState.update {
                    it.copy(
                        sections = separateToDailyGroupsUC(logs).toInternalSectionList(
                            prevCollapsedSections = it.getAllCollapsedDates()
                        ),
                        loadingStatus = InternalStatus.SUCCESS
                    )
                }
            }
            .catch { ex -> modelState.update { it.copy(loadingStatus = InternalStatus.ERROR) } }
            .launchIn(viewModelScope)
    }

    private fun convertModelToUIState() {
        modelState
            .onEach { internalState ->
                update { uiState ->
                    uiState.copy(
                        sections = internalState.sections.toUIStateSectionList(
                            dateToGroupTitleMapper
                        ),
                        loadingStatus = internalState.loadingStatus.toUIStateStatus(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    internal data class ModelState(
        val sections: List<ModelSection>,
        val loadingStatus: InternalStatus,
    ) {
        companion object {
            fun initialState(): ModelState {
                return ModelState(
                    sections = emptyList(),
                    loadingStatus = InternalStatus.LOADING,
                )
            }
        }

        data class ModelSection(
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
