package com.robgasp.dailylog.core.provider

import kotlinx.datetime.TimeZone

interface TimeZoneProvider : Provider<TimeZone, Unit> {
    override fun get(param: Unit?): TimeZone {
        return TimeZone.Companion.currentSystemDefault()
    }
}
