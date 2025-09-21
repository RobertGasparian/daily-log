package com.robgasp.dailylog.util

import android.content.Context
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.core.provider.TimeZoneProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@Singleton
@OptIn(ExperimentalTime::class)
class DateToTextMapper @Inject constructor(
    @param:ApplicationContext private val appContext: Context,
    private val timeZoneProvider: TimeZoneProvider,
) : Mapper<LocalDate, String> {

    private val androidFormatter by lazy {
        android.text.format.DateFormat.getDateFormat(appContext).apply {
            isLenient = false
        }
    }

    override fun mapTo(value: LocalDate): String {
        val tz = timeZoneProvider.get()

        // Format using the user’s preferred date pattern
        // TODO: hide under interface as well
        val millis = value.atStartOfDayIn(tz).toEpochMilliseconds()
        return androidFormatter.format(Date(millis))
    }

    override fun mapFrom(value: String): LocalDate {
        val trimmedValue = value.trim()
        val tz = timeZoneProvider.get()

        // TODO: hide under interface as well
        val parsed = androidFormatter.parse(trimmedValue)
            ?: throw IllegalArgumentException("Unrecognized date string for current locale: '$value'")
        return Instant.Companion.fromEpochMilliseconds(parsed.time)
            .toLocalDateTime(tz)
            .date
    }
}
