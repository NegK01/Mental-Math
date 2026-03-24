package com.negk01.mentalmath.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.negk01.mentalmath.domain.repository.GameRecordRepository

class HomeViewModelFactory(
    private val gameRecordRepository: GameRecordRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(gameRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}