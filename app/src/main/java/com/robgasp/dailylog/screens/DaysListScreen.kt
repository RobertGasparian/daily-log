package com.robgasp.dailylog.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DaysListScreen(days: List<Pair<String, Color>>, modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(count = days.size) { index ->
            val item = days[index]
            Day(item.first, item.second)
        }
    }
}

@Composable
private fun Day(date: String, color: Color, modifier: Modifier = Modifier) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
//                .background(color)
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Text(
                text = "current date: $date",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterStart),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = "Name - optional",
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.End)
                )
                CircleIndicator(
                    color = color,
                    size = 16.dp,
                    modifier = modifier
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun CircleIndicator(color: Color, size: Dp, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(size),
        onDraw = {
            drawCircle(color)
        }
    )
}

@Preview
@Composable
fun DaysListPreview() {
    DaysListScreen(
        listOf(
            "Day 1" to Color.Red,
            "Day 2" to Color.Blue,
            "Day 3" to Color.Yellow,
            "Day 4" to Color.Green,
        )
    )
}
