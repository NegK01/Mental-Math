package com.negk01.mentalmath.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.negk01.mentalmath.domain.repository.GameRecordRepository

class HistoryViewModelFactory(
    private val gameRecordRepository: GameRecordRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(gameRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}