package com.negk01.mentalmath.data.repository

import com.negk01.mentalmath.data.local.dao.GameRecordDao
import com.negk01.mentalmath.data.mapper.toDomain
import com.negk01.mentalmath.data.mapper.toEntity
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRecordRepositoryImpl(
    private val gameRecordDao: GameRecordDao
) : GameRecordRepository {

    override suspend fun insert(record: GameRecord) {
        gameRecordDao.insert(record.toEntity())
    }

    override fun getLastThreeRecords(): Flow<List<GameRecord>> {
        return gameRecordDao.getLastThreeRecords().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllRecords(): Flow<List<GameRecord>> {
        return gameRecordDao.getAllRecords().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun clearAll() {
        gameRecordDao.clearAll()
    }
}