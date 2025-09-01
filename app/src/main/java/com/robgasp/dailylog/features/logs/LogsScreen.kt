package com.robgasp.dailylog.features.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LogsScreen(viewModel: LogsViewModel, modifier: Modifier = Modifier, onNext: (id: String) -> Unit) {
    Box(modifier.fillMaxSize().background(Color.Yellow)) {
        Button(modifier = Modifier.align(Alignment.Center), onClick = { onNext("Some id (for now)") }) {
            Text("To Details")
        }
    }
}
