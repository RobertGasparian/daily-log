package com.robgasp.dailylog.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun LogTimePicker(
    modifier: Modifier = Modifier,
    initialTime: LocalTime? = null,
    is24Hour: Boolean = true,
    onPick: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val hour: Int
    val minute: Int
    if (initialTime == null) {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()
        val local = now.toLocalDateTime(tz)
        hour = local.hour
        minute = local.minute
    } else {
        hour = initialTime.hour
        minute = initialTime.minute
    }


    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = is24Hour
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimePicker(
                    state = timePickerState,
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.padding(horizontal = 8.dp)
                ) {
                    Button(modifier = Modifier.weight(1f), onClick = onDismiss) {
                        Text("Dismiss")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(modifier = Modifier.weight(1f), onClick = {
                        onPick(
                            LocalTime(
                                hour = timePickerState.hour,
                                minute = timePickerState.minute
                            )
                        )
                        onDismiss()
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
