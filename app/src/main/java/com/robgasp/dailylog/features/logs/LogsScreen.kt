package com.robgasp.dailylog.features.logs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robgasp.dailylog.R
import com.robgasp.dailylog.core.ui.ErrorDialog
import com.robgasp.dailylog.core.ui.HorizontalDotSpacer
import com.robgasp.dailylog.model.DLog

@Composable
fun LogsScreen(vm: LogsViewModel, modifier: Modifier = Modifier, onNext: (id: String) -> Unit) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(vm) {
        vm.events.collect {
            when (it) {
                is LogsViewModel.Navigate -> onNext(it.logId)
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (state.loadingStatus) {
            LogsViewModel.UIState.Status.LOADING -> {
                CircularProgressIndicator(strokeWidth = 4.dp)
            }

            LogsViewModel.UIState.Status.SUCCESS -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    state.sections.forEach { section ->
                        stickyHeader(key = section.date) {
                            val collapseIconRotation by animateFloatAsState(
                                targetValue = if (section.isCollapsed) -90f else 0f,
                                label = "collapseIconRotation"
                            )
                            Surface(
                                color = Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 8.dp)
                                    )
                                    Text(
                                        text = "${section.logs.size}",
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = "Collapse/Expand Arrow",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(4.dp)
                                            .rotate(collapseIconRotation)
                                            .clickable {
                                                vm.toggleGroup(section.date)
                                            }
                                    )
                                }
                            }
                        }

                        itemsIndexed(
                            items = section.logs,
                            key = { _, log -> log.id }
                        ) { index, log ->
                            AnimatedVisibility(
                                visible = !section.isCollapsed,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Column(Modifier.fillMaxSize()) {
                                    LogItem(log) {
                                        vm.openDetailedLog(log)
                                    }
                                    Spacer(
                                        Modifier.height(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            LogsViewModel.UIState.Status.ERROR -> {
                ErrorDialog(
                    title = "Loading Error",
                    actionLabel = "Ok",
                    cancelable = false,
                    onAction = vm::errorDismiss
                )
            }
        }
    }
}

@Composable
fun LogItem(
    log: DLog,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.06f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.tag),
                contentDescription = "Tag",
                tint = Color.White
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = log.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = log.logDate.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            HorizontalDotSpacer(
                modifier = Modifier
                    .width(32.dp),
                dotSize = 4.dp,
                dotColor = Color.White,
            )
            Text(
                text = log.logTime.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
