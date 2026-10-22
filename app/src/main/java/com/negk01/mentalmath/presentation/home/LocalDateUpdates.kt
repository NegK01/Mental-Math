package com.negk01.mentalmath.presentation.home

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime

fun localDateUpdates(now: () -> ZonedDateTime = { ZonedDateTime.now() }): Flow<LocalDate> = flow {
    while (currentCoroutineContext().isActive) {
        val current = now()
        val today = current.toLocalDate()
        emit(today)
        val nextMidnight = today.plusDays(1).atStartOfDay(current.zone)
        delay(Duration.between(current, nextMidnight).toMillis().coerceAtLeast(1L))
    }
}.distinctUntilChanged()
