package com.robgasp.dailylog.features.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateScreen(viewModel: CreateViewModel = viewModel(), modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().background(Color.Red)) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Create Screen")
        }
    }
}
