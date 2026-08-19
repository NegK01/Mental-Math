package com.negk01.mentalmath.domain.repository

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    suspend fun insert(record: GameRecord)

    // Flow completo — para HomeViewModel (racha + recientes) y métricas globales en HistoryViewModel
    fun getAllRecords(): Flow<List<GameRecord>>

    // Paginado — para la lista visual en History
    suspend fun getRecordsPaged(limit: Int, offset: Int): List<GameRecord>

    suspend fun clearAll()

    suspend fun getBestRecordForDifficulty(difficulty: Difficulty): GameRecord?

    suspend fun getPreviousBestRecordForDifficulty(difficulty: Difficulty): GameRecord?
}