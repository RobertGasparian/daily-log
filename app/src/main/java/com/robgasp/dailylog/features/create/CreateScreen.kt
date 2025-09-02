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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robgasp.dailylog.core.ui.LogDatePicker
import com.robgasp.dailylog.core.ui.LogTimePicker
import com.robgasp.dailylog.util.DoNothing
import com.robgasp.dailylog.util.showDismissableSnackBar
import timber.log.Timber

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    vm: CreateViewModel = hiltViewModel<CreateViewModel>()
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
            onValueChange = { vm.updateTitle(it) },
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
            onValueChange = { vm.updateDescription(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            placeholder = { Text("Your Log") }
        )
        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = {
                vm.showTimePicker()
            }) {
                Text("Pick a time")
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = state.time.toString(),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Button(onClick = {
                vm.showDatePicker()
            }) {
                Text("Pick a day")
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = state.day.toString(),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Button(onClick = vm::saveLog) {
            Text("Save")
        }
        Spacer(Modifier.weight(1f))
    }
    when (state.dialogStatus) {
        CreateViewModel.UIState.DialogStatus.TIME_PICKER -> {
            LogTimePicker(
                initialTime = state.time,
                is24Hour = true,
                onPick = {
                    vm.updateTime(it)
                },
                onDismiss = {
                    vm.dismissCurrentDialog()
                }
            )
        }

        CreateViewModel.UIState.DialogStatus.DATE_PICKER -> {
            LogDatePicker(
                initialDate = state.day,
                onPick = {
                    vm.updateDay(it)
                },
                onDismiss = {
                    vm.dismissCurrentDialog()
                }
            )
        }

        CreateViewModel.UIState.DialogStatus.NONE -> {
            DoNothing
        }
    }
}

@Preview
@Composable
private fun CreateScreenPreview() {
    CreateScreen()
}
