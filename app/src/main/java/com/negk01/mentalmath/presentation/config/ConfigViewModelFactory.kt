package com.negk01.mentalmath.presentation.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import com.negk01.mentalmath.domain.repository.SettingsRepository

class ConfigViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val gameRecordRepository: GameRecordRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfigViewModel::class.java)) {
            return ConfigViewModel(
                settingsRepository = settingsRepository,
                gameRecordRepository = gameRecordRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}