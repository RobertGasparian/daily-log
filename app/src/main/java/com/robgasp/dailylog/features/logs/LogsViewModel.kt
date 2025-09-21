package com.robgasp.dailylog.features.logs

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.ModelStateViewModel
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.domain.GetDLogsListUseCase
import com.robgasp.dailylog.domain.SeparateToDailyGroupsUseCase
import com.robgasp.dailylog.features.logs.LogsViewModel.ModelState.ModelStatus
import com.robgasp.dailylog.features.logs.di.LogsModule.Companion.GROUP_TITLE_MAPPER
import com.robgasp.dailylog.model.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Named

// TODO: error handling is absent for now
@HiltViewModel
class LogsViewModel @Inject constructor(
    private val getLogsListUC: GetDLogsListUseCase,
    private val separateToDailyGroupsUC: SeparateToDailyGroupsUseCase,
    private val timeToTextMapper: Mapper<LocalTime, String>,
    @param:Named(GROUP_TITLE_MAPPER) private val dateToGroupTitleMapper: Mapper<LocalDate, String>,
) : ModelStateViewModel<LogsViewModel.UIState, LogsViewModel.Event, LogsViewModel.Action, LogsScreenIntents, LogsViewModel.ModelState>(
    UIState.initialState(),
    ModelState.initialState()
) {
    init {
        loadData()
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
                        loadingStatus = ModelStatus.SUCCESS
                    )
                }
            }
            .catch { ex -> modelState.update { it.copy(loadingStatus = ModelStatus.ERROR) } }
            .launchIn(viewModelScope)
    }

    data class ModelState(
        val sections: List<ModelSection>,
        val loadingStatus: ModelStatus,
    ) {
        companion object {
            fun initialState(): ModelState {
                return ModelState(
                    sections = emptyList(),
                    loadingStatus = ModelStatus.LOADING,
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

        enum class ModelStatus {
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

    override val converter: (ModelState) -> UIState
        get() = { modelState ->
            UIState(
                sections = modelState.sections.toUIStateSectionList(
                    dateToGroupTitleMapper,
                    timeToTextMapper,
                ),
                loadingStatus = modelState.loadingStatus.toUIStateStatus(),

            )
        }

    sealed interface Event
    data class Navigate(val logId: String) : Event
}
