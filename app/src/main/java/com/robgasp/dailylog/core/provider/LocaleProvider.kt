package com.robgasp.dailylog.core.provider

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface LocaleProvider : Provider<Locale, Unit>

@Singleton
class LocaleProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : LocaleProvider {
    override fun get(param: Unit?): Locale {
        return context.resources.configuration.locales.let {
            if (it.isEmpty) Locale.getDefault() else it[0]
        }
    }
}
