package com.robgasp.dailylog.features.logs

import android.content.Context
import android.text.format.DateFormat
import com.robgasp.dailylog.core.provider.InstantProvider
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.core.provider.LabelProvider
import com.robgasp.dailylog.core.provider.LabelProvider.Label.TODAY
import com.robgasp.dailylog.core.provider.LabelProvider.Label.YESTERDAY
import com.robgasp.dailylog.core.provider.TimeZoneProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Singleton
@OptIn(ExperimentalTime::class)
class DateToGroupTitleMapper @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val timeZoneProvider: TimeZoneProvider,
    private val instantProvider: InstantProvider,
    private val labelProvider: LabelProvider,
) : Mapper<LocalDate, String> {

    override fun mapTo(value: LocalDate): String {
        val tz = timeZoneProvider.get()
        val today = instantProvider.get().toLocalDateTime(tz).date
        val yesterday = today.minus(1, DateTimeUnit.DAY)

        if (value == today) return labelProvider.get(TODAY)
        if (value == yesterday) return labelProvider.get(YESTERDAY)

        // Format using the user’s preferred date pattern
        // TODO: hide under interface as well
        val millis = value.atStartOfDayIn(tz).toEpochMilliseconds()
        val androidFormatter = DateFormat.getDateFormat(appContext)
        return androidFormatter.format(java.util.Date(millis))
    }

    override fun mapFrom(value: String): LocalDate {
        val trimmedValue = value.trim()
        val tz = timeZoneProvider.get()
        val today = instantProvider.get().toLocalDateTime(tz).date

        if (trimmedValue.equals(labelProvider.get(TODAY), ignoreCase = true)) return today
        if (trimmedValue.equals(labelProvider.get(YESTERDAY), ignoreCase = true)) {
            return today.minus(
                1,
                DateTimeUnit.DAY
            )
        }

        // TODO: hide under interface as well
        val androidFormatter = DateFormat.getDateFormat(appContext).apply {
            isLenient = false
        }
        val parsed = androidFormatter.parse(trimmedValue)
            ?: throw IllegalArgumentException("Unrecognized date string for current locale: '$value'")
        return Instant.fromEpochMilliseconds(parsed.time)
            .toLocalDateTime(tz)
            .date
    }
}
