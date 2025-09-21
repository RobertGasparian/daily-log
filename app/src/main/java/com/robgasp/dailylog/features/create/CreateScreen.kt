package com.robgasp.dailylog.features.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robgasp.dailylog.core.ui.LogDatePicker
import com.robgasp.dailylog.core.ui.LogTimePicker
import com.robgasp.dailylog.util.DoNothing
import com.robgasp.dailylog.util.showDismissableSnackBar
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import timber.log.Timber

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    vm: CreateViewModel,
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(vm) {
        Timber.i("CreateScreen LaunchedEffect")
        vm.events.collect { event ->
            Timber.i("CreateScreen Event: $event")
            when (event) {
                is CreateViewModel.Error -> {
                    showDismissableSnackBar(snackbarHostState, event.message ?: "Unknown Error")
                }

                CreateViewModel.Saved -> {
                    showDismissableSnackBar(snackbarHostState, "Saved successfully")
                }
            }
        }
    }
    CreateScreen(state, snackbarHostState, focusManager, modifier, vm.intents)
}

@Composable
fun CreateScreen(
    state: CreateViewModel.UIState,
    snackbarHostState: SnackbarHostState,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    intents: CreateScreenIntents? = null
) {
    // TODO: research if it is ok to create box just for the SnackbarHost at the bottom
    Box(
        modifier
            .fillMaxSize()
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//                .navigationBarsPadding() // avoid the system nav bar
                .imePadding() // avoid the on-screen keyboard
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = { intents?.updateTitle(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            placeholder = { Text("Log Title") }
        )
        Spacer(Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.description,
            onValueChange = { intents?.updateDescription(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            placeholder = { Text("Your Log") }
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = {
                intents?.showTimePicker()
            }) {
                Text("Pick a time")
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = state.time.toString(),
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Button(onClick = {
                intents?.showDatePicker()
            }) {
                Text("Pick a day")
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = state.day.toString(),
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Button(onClick = { intents?.saveLog() }) {
            Text("Save")
        }
        Spacer(Modifier.weight(1f))
    }
    when (val status = state.dialogStatus) {
        is CreateViewModel.UIState.DialogStatus.TimePicker -> {
            LogTimePicker(
                initialTime = status.time,
                is24Hour = true,
                onPick = {
                    intents?.updateTime(it)
                },
                onDismiss = {
                    intents?.dismissCurrentDialog()
                }
            )
        }

        is CreateViewModel.UIState.DialogStatus.DatePicker -> {
            LogDatePicker(
                initialDate = status.day,
                onPick = {
                    intents?.updateDay(it)
                },
                onDismiss = {
                    intents?.dismissCurrentDialog()
                }
            )
        }

        CreateViewModel.UIState.DialogStatus.None -> {
            DoNothing
        }
    }
}

@Stable
interface CreateScreenIntents {
    fun updateTitle(title: String)
    fun updateDescription(description: String)
    fun showTimePicker()
    fun showDatePicker()
    fun saveLog()
    fun updateTime(time: LocalTime)
    fun updateDay(date: LocalDate)
    fun dismissCurrentDialog()
}

@Preview
@Composable
private fun CreateScreenPreview() {
    CreateScreen(
        state = CreateViewModel.UIState(
            title = "Create New Log",
            description = "",
            time = "14:00",
            day = "11/22/2021",
            dialogStatus = CreateViewModel.UIState.DialogStatus.None
        ),
        snackbarHostState = SnackbarHostState(),
        focusManager = LocalFocusManager.current,
        intents = null
    )
}
