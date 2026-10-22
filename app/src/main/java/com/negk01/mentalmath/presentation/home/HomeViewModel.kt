package com.negk01.mentalmath.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.game.DailyStreakCalculator
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import com.negk01.mentalmath.domain.repository.SettingsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(
    private val gameRecordRepository: GameRecordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var isSupportCardDismissed = false
    private var totalRecordsCount = 0
    private val referenceDate = MutableStateFlow(LocalDate.now())

    init {
        loadSettingsFlags()
        observeRecentRecords()
        observeDateChanges()
    }

    private fun loadSettingsFlags() {
        viewModelScope.launch {
            try {
                val settings = settingsRepository.getSettings()
                isSupportCardDismissed = settings.hasDismissedHomeSupportCard
                _uiState.update {
                    it.copy(
                        showOnboarding = !settings.hasSeenOnboarding,
                        showSupportCard = !isSupportCardDismissed && totalRecordsCount >= 5
                    )
                }
            } catch (e: Exception) {
                Log.w("HomeViewModel", "Failed to load settings flags", e)
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

    fun dismissSupportCard() {
        isSupportCardDismissed = true
        _uiState.update { it.copy(showSupportCard = false) }
        viewModelScope.launch {
            try {
                settingsRepository.markHomeSupportCardDismissed()
            } catch (e: Exception) {
                Log.w("HomeViewModel", "Failed to mark home support card dismissed", e)
            }
        }
    }

    private fun observeDateChanges() {
        viewModelScope.launch {
            _uiState.subscriptionCount.collectLatest { subscribers ->
                if (subscribers > 0) {
                    localDateUpdates().collect { today ->
                        referenceDate.value = today
                    }
                }
            }
        }
    }

    private fun observeRecentRecords() {
        viewModelScope.launch {
            try {
                combine(gameRecordRepository.getAllRecords(), referenceDate) { records, today ->
                    records to today
                }.collect { (allRecords, today) ->
                    totalRecordsCount = allRecords.size
                    val recentRecords = allRecords.take(3)
                    val streakResult = withContext(Dispatchers.Default) {
                        DailyStreakCalculator.calculate(
                            timestamps = allRecords.map { it.playedAt },
                            referenceDate = today
                        )
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            today = today,
                            dailyStreak = streakResult.streak,
                            recentRecords = recentRecords,
                            streakStartDate = streakResult.startDate,
                            streakEndDate = streakResult.endDate,
                            showSupportCard = !isSupportCardDismissed && totalRecordsCount >= 5
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w("HomeViewModel", "Failed to observe recent records", e)
            }
        }
    }

    fun showCalendarDialog() {
        _uiState.update { it.copy(showCalendarDialog = true) }
    }

    fun hideCalendarDialog() {
        _uiState.update { it.copy(showCalendarDialog = false) }
    }

    fun showSupportDialog() {
        _uiState.update { it.copy(showSupportDialog = true) }
    }

    fun hideSupportDialog() {
        _uiState.update { it.copy(showSupportDialog = false) }
    }

    fun onDonationCompleted() {
        hideSupportDialog()
        isSupportCardDismissed = true
        _uiState.update { it.copy(showSupportCard = false) }
    }
}
