package com.robgasp.dailylog.features.logs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robgasp.dailylog.core.ui.ErrorDialog
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
                    state.logs.forEach { section ->
                        stickyHeader(key = section.date) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                Text(
                                    text = section.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }

                        itemsIndexed(items = section.logs, key = { _, log -> log.id }) { index, log ->
                            LogItem(log) {
                                vm.openDetailedLog(log)
                            }
                            if (index < section.logs.lastIndex) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
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
fun LogItem(log: DLog, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "title: ${log.title}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "dateTime: ${log.logDate} : ${log.logTime}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
