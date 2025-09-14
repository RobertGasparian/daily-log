package com.robgasp.dailylog.features.create

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.domain.SaveDLogUseCase
import com.robgasp.dailylog.core.provider.DateTimeProvider
import com.robgasp.dailylog.core.misc.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val saveDLogUC: SaveDLogUseCase,
    private val logValidator: Validator<UIState>,
) : BaseViewModel<CreateViewModel.UIState, CreateViewModel.Event, CreateViewModel.Action, CreateScreenIntents>(UIState.initialState(dateTimeProvider)) {

    override val intents: CreateScreenIntents
        get() = object : CreateScreenIntents {
            override fun updateTitle(title: String) = fire(Action.UpdateTitle(title))
            override fun updateDescription(description: String) = fire(Action.UpdateDescription(description))
            override fun showTimePicker() = fire(Action.ShowTimePicker)
            override fun showDatePicker() = fire(Action.ShowDatePicker)
            override fun saveLog() = fire(Action.SaveLog)
            override fun updateTime(time: LocalTime) = fire(Action.UpdateTime(time))
            override fun updateDay(date: LocalDate) = fire(Action.UpdateDay(date))
            override fun dismissCurrentDialog() = fire(Action.DismissDialog)
        }

    sealed interface Action {
        data object SaveLog : Action
        data object DismissDialog : Action
        data class UpdateTitle(val title: String) : Action
        data class UpdateDescription(val description: String) : Action
        data class UpdateTime(val time: LocalTime) : Action
        data class UpdateDay(val day: LocalDate) : Action
        data object ShowTimePicker : Action
        data object ShowDatePicker : Action
    }

    override fun reduce(action: Action) {
        when (action) {
            Action.DismissDialog -> update { it.copy(dialogStatus = UIState.DialogStatus.NONE) }
            Action.ShowDatePicker -> update { it.copy(dialogStatus = UIState.DialogStatus.DATE_PICKER) }
            Action.ShowTimePicker -> update { it.copy(dialogStatus = UIState.DialogStatus.TIME_PICKER) }
            Action.SaveLog -> saveLog()
            is Action.UpdateDay -> update { it.copy(day = action.day) }
            is Action.UpdateDescription -> {
                update { it.copy(description = action.description) }
            }
            is Action.UpdateTime -> update { it.copy(time = action.time) }
            is Action.UpdateTitle -> update { it.copy(title = action.title) }
        }
    }

    private fun saveLog() {
        val report = logValidator.isValid(uiState.value)
        when (report) {
            is Validator.Invalid -> {
                report.reportMessages.forEach { message ->
                    post(Error(message))
                    // TODO: figure out typesafe reporting mechanism
                }
            }
            Validator.Valid -> {
                viewModelScope.launch {
                    saveDLogUC(uiState.value.toDLog(dateTimeProvider))
                    post(Saved)
                }
            }
        }
    }

    data class UIState(
        val title: String,
        val description: String,
        val time: LocalTime,
        val day: LocalDate,
        val dialogStatus: DialogStatus,
    ) {
        companion object {
            fun initialState(dateTimeProvider: DateTimeProvider): UIState {
                return UIState(
                    title = "",
                    description = "",
                    time = dateTimeProvider.currentTime(),
                    day = dateTimeProvider.currentDate(),
                    dialogStatus = DialogStatus.NONE,
                )
            }
        }

        enum class DialogStatus {
            TIME_PICKER,
            DATE_PICKER,
            NONE
        }
    }

    sealed interface Event
    data object Saved : Event
    data class Error(val message: String? = null) : Event
}
