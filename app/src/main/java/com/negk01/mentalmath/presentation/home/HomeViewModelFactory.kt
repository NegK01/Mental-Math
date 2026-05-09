package com.negk01.mentalmath.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import com.negk01.mentalmath.domain.repository.SettingsRepository

class HomeViewModelFactory(
    private val gameRecordRepository: GameRecordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(gameRecordRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
