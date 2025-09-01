package com.robgasp.dailylog.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomBar(tab: Tab, modifier: Modifier = Modifier) {
    TODO("implement custom bottom bar")
}

enum class Tab(val icon: ImageVector, val label: String) {
    LOGS(Icons.AutoMirrored.Filled.List, "Logs"),
    CREATE(Icons.Filled.Add, "Create"),
    INSIGHTS(Icons.Filled.Star, "Insights"),
}
