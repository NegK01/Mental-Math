package com.negk01.mentalmath.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import com.negk01.mentalmath.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HomeViewModel(
    private val gameRecordRepository: GameRecordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadOnboardingFlag()
        observeRecentRecords()
    }

    private fun loadOnboardingFlag() {
        viewModelScope.launch {
            try {
                val settings = settingsRepository.getSettings()
                _uiState.update { it.copy(showOnboarding = !settings.hasSeenOnboarding) }
            } catch (e: Exception) {
                Log.w("HomeViewModel", "Failed to load onboarding flag", e)
            }
        }
    }

    fun markOnboardingShown() {
        viewModelScope.launch {
            try {
                settingsRepository.markOnboardingShown()
                _uiState.update { it.copy(showOnboarding = false) }
            } catch (e: Exception) {
                Log.w("HomeViewModel", "Failed to mark onboarding shown", e)
            }
        }
    }

    private fun observeRecentRecords() {
        viewModelScope.launch {
            try {
                gameRecordRepository.getAllRecords().collect { allRecords ->
                    val recentRecords = allRecords.take(3)
                    val streakResult = calculateDailyStreak(
                        timestamps = allRecords.map { it.playedAt }
                    )

                    _uiState.update { currentState ->
                        currentState.copy(
                            dailyStreak = streakResult.streak,
                            recentRecords = recentRecords,
                            streakStartDate = streakResult.startDate,
                            streakEndDate = streakResult.endDate
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("HomeViewModel", "Failed to observe recent records", e)
            }
        }
    }

    private data class StreakResult(
        val streak: Int,
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    private fun calculateDailyStreak(timestamps: List<Long>): StreakResult {
        if (timestamps.isEmpty()) return StreakResult(0, null, null)

        val zoneId = ZoneId.systemDefault()

        val uniqueDates = timestamps
            .map { Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate() }
            .distinct()
            .sortedDescending()

        val today = LocalDate.now(zoneId)
        val yesterday = today.minusDays(1)

        val startsTodayOrYesterday = uniqueDates.firstOrNull() == today || uniqueDates.firstOrNull() == yesterday
        if (!startsTodayOrYesterday) return StreakResult(0, null, null)

        var streak = 1
        var expectedDate = uniqueDates.first()

        for (i in 1 until uniqueDates.size) {
            val nextExpected = expectedDate.minusDays(1)
            if (uniqueDates[i] == nextExpected) {
                streak++
                expectedDate = uniqueDates[i]
            } else {
                break
            }
        }

        return StreakResult(streak, expectedDate, uniqueDates.first())
    }

    fun showCalendarDialog() {
        _uiState.update { it.copy(showCalendarDialog = true) }
    }

    fun hideCalendarDialog() {
        _uiState.update { it.copy(showCalendarDialog = false) }
    }
}
