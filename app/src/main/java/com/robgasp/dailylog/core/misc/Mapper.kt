package com.robgasp.dailylog.core.misc

interface Mapper<T, R> {
    fun mapTo(value: T): R
    fun mapFrom(value: R): T

    fun mapToList(list: List<T>): List<R> = list.map { mapTo(it) }
    fun mapFromList(list: List<R>): List<T> = list.map { mapFrom(it) }
}
