package com.robgasp.dailylog.util

interface Validator<T> {
    fun isValid(value: T): Report

    sealed interface Report
    data object Valid : Report
    data class Invalid(
        val reportMessages: List<String> = emptyList(),
    ) : Report
}
