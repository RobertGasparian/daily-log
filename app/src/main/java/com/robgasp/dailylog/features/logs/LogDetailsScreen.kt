package com.robgasp.dailylog.features.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LogDetailsScreen(viewModel: LogDetailsViewModel, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().background(Color.Blue)) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Log Details Screen")
        }
    }
}
