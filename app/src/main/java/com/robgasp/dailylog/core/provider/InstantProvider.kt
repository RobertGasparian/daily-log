package com.robgasp.dailylog.core.provider

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface InstantProvider : Provider<Instant, Unit> {
    override fun get(param: Unit?): Instant {
        return Clock.System.now()
    }
}
