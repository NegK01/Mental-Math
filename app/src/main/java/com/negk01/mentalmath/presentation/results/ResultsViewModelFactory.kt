package com.negk01.mentalmath.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.negk01.mentalmath.domain.repository.GameRecordRepository

class ResultsViewModelFactory(
    private val gameRecordRepository: GameRecordRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            return ResultsViewModel(gameRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
