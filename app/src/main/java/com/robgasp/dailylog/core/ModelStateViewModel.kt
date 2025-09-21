package com.robgasp.dailylog.core

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

abstract class ModelStateViewModel<UIState, Event, Action, Intents, ModelState>(initialUiState: UIState, initialModelState: ModelState) :
    BaseViewModel<UIState, Event, Action, Intents>(initialUiState) {

    protected val modelState: MutableStateFlow<ModelState> = MutableStateFlow(initialModelState)

    abstract val converter: (ModelState) -> UIState

    init {
        convertModelToUIState(converter)
    }

    protected fun updateModel(block: (ModelState) -> ModelState) {
        modelState.update {
            block(it)
        }
    }

    protected fun convertModelToUIState(converter: (ModelState) -> UIState) {
        modelState
            .drop(1)
            .onEach { modelState ->
                update { uiState ->
                    converter(modelState)
                }
            }
            .launchIn(viewModelScope)
    }
}
