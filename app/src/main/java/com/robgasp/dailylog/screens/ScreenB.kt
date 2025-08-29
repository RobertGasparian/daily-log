package com.robgasp.dailylog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScreenB(id: String, modifier: Modifier = Modifier, onNext: () -> Unit) {
    Box(modifier.fillMaxSize().background(Color.Red)) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("current id is: $id")
            Spacer(Modifier.height(20.dp))
            Button(onClick = onNext) {
                Text("To Screen C")
            }
        }
    }
}
