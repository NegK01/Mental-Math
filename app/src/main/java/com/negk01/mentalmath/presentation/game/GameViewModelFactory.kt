package com.negk01.mentalmath.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.negk01.mentalmath.domain.repository.GameRecordRepository

class GameViewModelFactory(
    private val gameRecordRepository: GameRecordRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(gameRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}