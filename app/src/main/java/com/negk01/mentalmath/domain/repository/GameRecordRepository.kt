package com.negk01.mentalmath.domain.repository

import com.negk01.mentalmath.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    suspend fun insert(record: GameRecord)
    fun getLastThreeRecords(): Flow<List<GameRecord>>
    fun getAllRecords(): Flow<List<GameRecord>>
    suspend fun clearAll()
}