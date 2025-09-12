package com.robgasp.dailylog.core.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val navPanelWithCutoutHeight = 104.dp
val navPanelWithCutoutFabSize = 56.dp

val bgGradientStart = Color(0xFF151D4F)
val bgGradientMiddle = Color(0xFF161E5B)
val bgGradientEnd = Color(0xFF2F314C)

val BackgroundGradient = Brush.verticalGradient(
    listOf(bgGradientStart, bgGradientMiddle, bgGradientEnd)
)
