package com.robgasp.dailylog.util

import android.content.Context
import android.text.format.DateFormat
import com.robgasp.dailylog.core.misc.Mapper
import com.robgasp.dailylog.core.provider.LocaleProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.datetime.LocalTime as KxLocalTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import java.text.SimpleDateFormat
import java.time.LocalTime as JLocalTime
import java.time.format.DateTimeFormatterBuilder
import kotlin.getValue

@Singleton
class TimeToTextMapper @Inject constructor(
    @param:ApplicationContext private val appContext: Context,
    private val localeProvider: LocaleProvider,
) : Mapper<KxLocalTime, String> {

    private val formatter by lazy {
        val pattern = (DateFormat.getTimeFormat(appContext) as SimpleDateFormat).toLocalizedPattern()
        DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern(pattern)
            .toFormatter(localeProvider.get())
    }

    override fun mapTo(value: KxLocalTime): String {
        return value.toJavaLocalTime().format(formatter)
    }

    override fun mapFrom(value: String): KxLocalTime {
        val normalized = value.trim().replace(Regex("\\p{Z}+"), " ")
        return JLocalTime.parse(normalized, formatter).toKotlinLocalTime()
    }
}
