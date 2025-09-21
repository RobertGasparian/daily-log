package com.robgasp.dailylog.util

import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.core.provider.InstantProvider
import com.robgasp.dailylog.core.provider.LabelProvider
import com.robgasp.dailylog.core.provider.TimeZoneProvider
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.ExperimentalTime

@Singleton
@OptIn(ExperimentalTime::class)
class DateToGroupTitleMapper @Inject constructor(
    private val timeZoneProvider: TimeZoneProvider,
    private val instantProvider: InstantProvider,
    private val labelProvider: LabelProvider,
    private val dateToTextMapper: Mapper<LocalDate, String>,
) : Mapper<LocalDate, String> {

    override fun mapTo(value: LocalDate): String {
        val tz = timeZoneProvider.get()
        val today = instantProvider.get().toLocalDateTime(tz).date
        val yesterday = today.minus(1, DateTimeUnit.Companion.DAY)

        if (value == today) return labelProvider.get(LabelProvider.Label.TODAY)
        if (value == yesterday) return labelProvider.get(LabelProvider.Label.YESTERDAY)

        return dateToTextMapper.mapTo(value)
    }

    override fun mapFrom(value: String): LocalDate {
        val trimmedValue = value.trim()
        val tz = timeZoneProvider.get()
        val today = instantProvider.get().toLocalDateTime(tz).date

        if (trimmedValue.equals(labelProvider.get(LabelProvider.Label.TODAY), ignoreCase = true)) return today
        if (trimmedValue.equals(labelProvider.get(LabelProvider.Label.YESTERDAY), ignoreCase = true)) {
            return today.minus(
                1,
                DateTimeUnit.Companion.DAY
            )
        }
        return dateToTextMapper.mapFrom(value)
    }
}
