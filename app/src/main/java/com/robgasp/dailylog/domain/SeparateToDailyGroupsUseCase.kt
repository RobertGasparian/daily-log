package com.robgasp.dailylog.domain

import com.robgasp.dailylog.model.DLog
import kotlinx.datetime.LocalDate
import java.util.SortedMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeparateToDailyGroupsUseCase @Inject constructor() {
    operator fun invoke(logs: List<DLog>): SortedMap<LocalDate, List<DLog>> {
        return logs.groupBy { it.logDate }.toSortedMap(compareByDescending { it })
    }
}
