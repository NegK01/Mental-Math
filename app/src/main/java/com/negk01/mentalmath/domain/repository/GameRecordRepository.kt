package com.negk01.mentalmath.domain.repository

import com.negk01.mentalmath.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    suspend fun insert(record: GameRecord)
    fun getLastThreeRecords(): Flow<List<GameRecord>>

    // Flow completo — solo para métricas globales en HistoryViewModel
    fun getAllRecords(): Flow<List<GameRecord>>

    // Paginado — para la lista visual en History
    suspend fun getRecordsPaged(limit: Int, offset: Int): List<GameRecord>

    suspend fun clearAll()

    suspend fun getBestAccuracyForDifficulty(difficulty: Difficulty): Double?
}