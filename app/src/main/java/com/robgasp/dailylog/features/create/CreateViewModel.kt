package com.robgasp.dailylog.features.create

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.ModelStateViewModel
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.domain.SaveDLogUseCase
import com.robgasp.dailylog.core.provider.DateTimeProvider
import com.robgasp.dailylog.core.misc.Validator
import com.robgasp.dailylog.features.create.CreateViewModel.ModelState.ModelDialogStatus.None.toUIStateDialogStatus
import com.robgasp.dailylog.features.create.CreateViewModel.UIState.DialogStatus
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
    private val dateToTextMapper: Mapper<LocalDate, String>,
    private val timeToTextMapper: Mapper<LocalTime, String>,
) : ModelStateViewModel<CreateViewModel.UIState, CreateViewModel.Event, CreateViewModel.Action, CreateScreenIntents, CreateViewModel.ModelState>(
    UIState.initialState(dateTimeProvider, dateToTextMapper, timeToTextMapper),
    ModelState.initialState(dateTimeProvider)
) {
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
            Action.DismissDialog -> updateModel { it.copy(dialogStatus = ModelState.ModelDialogStatus.None) }
            Action.ShowDatePicker -> updateModel { it.copy(dialogStatus = ModelState.ModelDialogStatus.DatePicker(it.day)) }
            Action.ShowTimePicker -> updateModel { it.copy(dialogStatus = ModelState.ModelDialogStatus.TimePicker(it.time)) }
            Action.SaveLog -> saveLog()
            is Action.UpdateDay -> updateModel { it.copy(day = action.day) }
            is Action.UpdateDescription -> {
                updateModel { it.copy(description = action.description) }
            }

            is Action.UpdateTime -> updateModel { it.copy(time = action.time) }
            is Action.UpdateTitle -> updateModel { it.copy(title = action.title) }
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
                    saveDLogUC(modelState.value.toDLog(dateTimeProvider))
                    post(Saved)
                }
            }
        }
    }

    data class ModelState(
        val title: String,
        val description: String,
        val time: LocalTime,
        val day: LocalDate,
        val dialogStatus: ModelDialogStatus,
    ) {
        companion object {
            fun initialState(dateTimeProvider: DateTimeProvider): ModelState {
                return ModelState(
                    title = "",
                    description = "",
                    time = dateTimeProvider.currentTime(),
                    day = dateTimeProvider.currentDate(),
                    dialogStatus = ModelDialogStatus.None,
                )
            }
        }

        sealed class ModelDialogStatus {
            data class TimePicker(val time: LocalTime) : ModelDialogStatus()
            data class DatePicker(val day: LocalDate) : ModelDialogStatus()
            data object None : ModelDialogStatus()

            fun ModelDialogStatus.toUIStateDialogStatus(): DialogStatus {
                return when (this) {
                    is DatePicker -> DialogStatus.DatePicker(this.day)
                    is TimePicker -> DialogStatus.TimePicker(this.time)
                    is None -> DialogStatus.None
                }
            }
        }
    }

    data class UIState(
        val title: String,
        val description: String,
        val time: String,
        val day: String,
        val dialogStatus: DialogStatus,
    ) {
        companion object {
            fun initialState(
                dateTimeProvider: DateTimeProvider,
                dateToTextMapper: Mapper<LocalDate, String>,
                timeToTextMapper: Mapper<LocalTime, String>,
            ): UIState {
                return UIState(
                    title = "",
                    description = "",
                    time = timeToTextMapper.mapTo(dateTimeProvider.currentTime()),
                    day = dateToTextMapper.mapTo(dateTimeProvider.currentDate()),
                    dialogStatus = DialogStatus.None,
                )
            }
        }

        sealed class DialogStatus {
            data class TimePicker(val time: LocalTime) : DialogStatus()
            data class DatePicker(val day: LocalDate) : DialogStatus()
            data object None : DialogStatus()

            fun DialogStatus.toModelDialogStatus(): ModelState.ModelDialogStatus {
                return when (this) {
                    is DatePicker -> ModelState.ModelDialogStatus.DatePicker(this.day)
                    is TimePicker -> ModelState.ModelDialogStatus.TimePicker(this.time)
                    is None -> ModelState.ModelDialogStatus.None
                }
            }
        }
    }

    override val converter: (ModelState) -> UIState
        get() = { modelState ->
            UIState(
                title = modelState.title,
                description = modelState.description,
                time = timeToTextMapper.mapTo(modelState.time),
                day = dateToTextMapper.mapTo(modelState.day),
                dialogStatus = modelState.dialogStatus.toUIStateDialogStatus(),
            )
        }

    sealed interface Event
    data object Saved : Event
    data class Error(val message: String? = null) : Event
}
