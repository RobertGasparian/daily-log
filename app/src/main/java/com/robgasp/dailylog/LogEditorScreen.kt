package com.robgasp.dailylog

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.robgasp.dailylog.ui.theme.DailyLogTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogEditorScreen(modifier: Modifier = Modifier) {
    val datePickerState = rememberDatePickerState()
    DatePicker(datePickerState)
}

@Preview
@Composable
private fun LogEditorScreenPreview() {
    DailyLogTheme {
        LogEditorScreen()
    }
}

