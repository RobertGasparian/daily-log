package com.robgasp.dailylog.features.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LogDetailsScreen(vm: LogDetailsViewModel, modifier: Modifier = Modifier) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = state.title,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = state.description ?: "No description",
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = state.time,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = state.date,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = state.createdAt,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
        if (state.modifiedAt != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = state.modifiedAt!!,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
        }
    }
}
