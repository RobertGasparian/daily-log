package com.robgasp.dailylog.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun HorizontalDotSpacer(
    dotSize: Dp,
    dotColor: Color,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(dotSize)) {
            drawCircle(
                color = dotColor,
                radius = dotSize.toPx() / 2
            )
        }
    }
}