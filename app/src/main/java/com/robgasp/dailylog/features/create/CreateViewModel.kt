package com.robgasp.dailylog.features.create

import androidx.lifecycle.viewModelScope
import com.robgasp.dailylog.core.BaseViewModel
import com.robgasp.dailylog.domain.SaveDLogUseCase
import com.robgasp.dailylog.model.DLog
import com.robgasp.dailylog.core.provider.DateTimeProvider
import com.robgasp.dailylog.core.misc.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val saveDLogUC: SaveDLogUseCase,
    private val logValidator: Validator<UIState>,
) : BaseViewModel<CreateViewModel.UIState, CreateViewModel.Event>(UIState.initialState(dateTimeProvider)) {

    fun updateTitle(text: String) {
        update { it.copy(title = text) }
    }

    fun updateDescription(text: String) {
        update { it.copy(description = text) }
    }

    fun updateTime(time: LocalTime) {
        update { it.copy(time = time) }
    }

    fun updateDay(day: LocalDate) {
        update { it.copy(day = day) }
    }

    fun showTimePicker() {
        update { it.copy(dialogStatus = UIState.DialogStatus.TIME_PICKER) }
    }

    fun showDatePicker() {
        update { it.copy(dialogStatus = UIState.DialogStatus.DATE_PICKER) }
    }

    fun dismissCurrentDialog() {
        update { it.copy(dialogStatus = UIState.DialogStatus.NONE) }
    }

    fun saveLog() {
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

@OptIn(ExperimentalUuidApi::class)
fun CreateViewModel.UIState.toDLog(dateTimeProvider: DateTimeProvider): DLog {
    val id = Uuid.Companion.random().toString()
    return DLog(
        id = id,
        title = this.title,
        description = this.description,
        logTime = this.time,
        logDate = this.day,
        creationDate = dateTimeProvider.currentDateTime(),
        modificationDate = null
    )
}

