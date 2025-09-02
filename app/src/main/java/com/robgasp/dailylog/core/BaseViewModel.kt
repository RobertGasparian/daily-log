package com.robgasp.dailylog.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<State, Event>(initialState: State) : ViewModel() {

    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    protected val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    protected fun update(block: (State) -> State) {
        _uiState.update {
            block(it)
        }
    }

    protected fun post(event: Event) {
        viewModelScope.launch {
            Timber.i("post event: $event")
            _events.emit(event)
        }
    }

    init {
        Timber.d("init: $this")
    }

    override fun onCleared() {
        Timber.d("onCleared: $this")
        super.onCleared()
    }
}
