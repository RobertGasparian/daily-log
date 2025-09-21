package com.robgasp.dailylog.features.logs

import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.features.logs.LogsViewModel.ModelState
import com.robgasp.dailylog.features.logs.LogsViewModel.ModelState.ModelStatus
import com.robgasp.dailylog.features.logs.LogsViewModel.UIState
import com.robgasp.dailylog.model.DLog
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.util.SortedMap
import kotlin.collections.component1
import kotlin.collections.component2

internal fun List<DLog>.toUIStateLogList(
    timeMapper: Mapper<LocalTime, String>,
    dateMapper: Mapper<LocalDate, String>,
): List<UIState.UILog> {
    return this.map {
        UIState.UILog(
            id = it.id,
            title = it.title,
            description = it.description,
            time = timeMapper.mapTo(it.logTime),
            date = dateMapper.mapTo(it.logDate),
        )
    }
}

internal fun List<ModelState.ModelSection>.toUIStateSectionList(
    dateToGroupTitleMapper: Mapper<LocalDate, String>,
    timeToTextMapper: Mapper<LocalTime, String>,
): List<UIState.Section> {
    return this.map {
        UIState.Section(
            title = dateToGroupTitleMapper.mapTo(it.date),
            isCollapsed = it.isCollapsed,
            logs = it.logs.toUIStateLogList(timeToTextMapper, dateToGroupTitleMapper)
        )
    }
}

internal fun SortedMap<LocalDate, List<DLog>>.toInternalSectionList(
    prevCollapsedSections: Set<LocalDate> = emptySet(),
): List<ModelState.ModelSection> {
    return entries.toList().map { (date, logs) ->
        ModelState.ModelSection(
            date = date,
            logs = logs,
            isCollapsed = prevCollapsedSections.contains(date)
        )
    }
}

internal fun ModelStatus.toUIStateStatus(): UIState.Status {
    return when (this) {
        ModelStatus.LOADING -> UIState.Status.LOADING
        ModelStatus.SUCCESS -> UIState.Status.SUCCESS
        ModelStatus.ERROR -> UIState.Status.ERROR
    }
}

