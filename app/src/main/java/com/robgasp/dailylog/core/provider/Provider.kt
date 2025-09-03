package com.robgasp.dailylog.core.provider

interface Provider<T, Param> {
    fun get(param: Param? = null): T
}
