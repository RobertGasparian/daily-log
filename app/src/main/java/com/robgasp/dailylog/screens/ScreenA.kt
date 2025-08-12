package com.robgasp.dailylog.screens

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
fun ScreenA(modifier: Modifier = Modifier, onNext: (id: String) -> Unit) {
    Box(modifier.fillMaxSize().background(Color.Green)) {
        Button(modifier = Modifier.align(Alignment.Center),onClick = { onNext("Some id (for now)") }) {
            Text("To Screen B")
        }
    }
}