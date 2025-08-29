package com.robgasp.dailylog.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.robgasp.dailylog.LogEditorViewModel
import com.robgasp.dailylog.ui.theme.DailyLogTheme
import com.robgasp.dailylog.ui.widgets.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogEditorScreen(viewModel: LogEditorViewModel, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar() },
        content = { innerPadding ->
            LogEditor(Modifier.padding(innerPadding))
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        viewModel.addLog()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Log")
                    }
                }
            )
        }
    )
}

@Composable
fun LogEditor(modifier: Modifier = Modifier) {
}

@Preview
@Composable
private fun LogEditorScreenPreview() {
    DailyLogTheme {
        LogEditorScreen()
    }
}

