package com.negk01.mentalmath.presentation.game

import android.os.SystemClock

fun interface MonotonicClock {
    fun elapsedRealtime(): Long
}

object AndroidMonotonicClock : MonotonicClock {
    override fun elapsedRealtime(): Long = SystemClock.elapsedRealtime()
}
