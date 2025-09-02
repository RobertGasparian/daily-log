package com.robgasp.dailylog.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun LogDatePicker(
    modifier: Modifier = Modifier,
    initialDate: LocalDate? = null,
    onPick: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val tz = TimeZone.currentSystemDefault()
    val startOfTheDay: Long = if (initialDate == null) {
        val now = Clock.System.now()
        val local = now.toLocalDateTime(tz)
        local.date.atStartOfDayIn(tz).toEpochMilliseconds()
    } else {
        initialDate.atStartOfDayIn(tz).toEpochMilliseconds()
    }

    val datePickerState = rememberDatePickerState(startOfTheDay)
    val confirmIsEnabled by remember {
        derivedStateOf {
            datePickerState.selectedDateMillis != null
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DatePicker(
                    state = datePickerState,
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Button(modifier = Modifier.weight(1f), onClick = onDismiss) {
                        Text("Dismiss")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                val instant = Instant.fromEpochMilliseconds(it)
                                onPick(instant.toLocalDateTime(tz).date)
                                onDismiss()
                            }
                        },
                        enabled = confirmIsEnabled,
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
