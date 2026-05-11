package com.negk01.mentalmath.presentation.home

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
        // DEV: descomentar para ver el onboarding de nuevo en el próximo lanzamiento
        // viewModelScope.launch { settingsRepository.saveSettings(settingsRepository.getSettings().copy(hasSeenOnboarding = false)) }
        loadOnboardingFlag()
        observeRecentRecords()
    }

    private fun loadOnboardingFlag() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            _uiState.update { it.copy(showOnboarding = !settings.hasSeenOnboarding) }
        }
    }

    fun markOnboardingShown() {
        viewModelScope.launch {
            settingsRepository.markOnboardingShown()
            _uiState.update { it.copy(showOnboarding = false) }
        }
    }

    private fun observeRecentRecords() {
        viewModelScope.launch {
            gameRecordRepository.getAllRecords().collect { allRecords ->
                val recentRecords = allRecords.take(3)
                val dailyStreak = calculateDailyStreak(
                    timestamps = allRecords.map { it.playedAt }
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        dailyStreak = dailyStreak,
                        recentRecords = recentRecords
                    )
                }
            }
        }
    }

    private fun calculateDailyStreak(timestamps: List<Long>): Int {
        if (timestamps.isEmpty()) return 0

        val zoneId = ZoneId.systemDefault()

        val uniqueDates = timestamps
            .map { Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate() }
            .distinct()
            .sortedDescending()

        val today = LocalDate.now(zoneId)
        val yesterday = today.minusDays(1)

        val startsTodayOrYesterday = uniqueDates.firstOrNull() == today || uniqueDates.firstOrNull() == yesterday
        if (!startsTodayOrYesterday) return 0

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

        return streak
    }
}
