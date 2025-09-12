package com.robgasp.dailylog.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.BottomNavPanelWithCutOut(
    leftContent: @Composable RowScope.() -> Unit,
    rightContent: @Composable RowScope.() -> Unit,
    fabGapWidth: Dp = navPanelWithCutoutFabSize
) {
    BoxWithConstraints(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(navPanelWithCutoutHeight)
            .clip(
                BottomNavShape(
                    cornerRadius = with(LocalDensity.current) { 20.dp.toPx() },
                    dockRadius = with(LocalDensity.current) { 38.dp.toPx() },
                )
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val parentWidth = maxWidth
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.width((parentWidth - fabGapWidth) / 2),
                horizontalArrangement = Arrangement.Start,
            ) {
                leftContent()
            }
            Spacer(modifier = Modifier.width(fabGapWidth)) // middle spacer
            Row(
                Modifier.width((parentWidth - fabGapWidth) / 2),
                horizontalArrangement = Arrangement.End
            ) {
                rightContent()
            }
        }
    }
}

@Composable
fun BoxScope.BottomNavPanel(
    fabContent: @Composable () -> Unit,
    leftContent: @Composable RowScope.() -> Unit,
    rightContent: @Composable RowScope.() -> Unit,
    fabAction: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        BottomNavPanelWithCutOut(leftContent, rightContent, navPanelWithCutoutFabSize)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = navPanelWithCutoutHeight - navPanelWithCutoutFabSize / 2)
                .size(navPanelWithCutoutFabSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    fabAction()
                },
            contentAlignment = Alignment.Center,
        ) {
            fabContent()
        }
    }
}

enum class Tab(val icon: ImageVector, val label: String) {
    LOGS(Icons.AutoMirrored.Filled.List, "Logs"),
    INSIGHTS(Icons.Filled.Star, "Insights"),
}
