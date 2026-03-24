package com.negk01.mentalmath.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.negk01.mentalmath.data.local.entity.GameRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: GameRecordEntity)

    @Query("SELECT * FROM game_records ORDER BY playedAt DESC LIMIT 3")
    fun getLastThreeRecords(): Flow<List<GameRecordEntity>>

    @Query("SELECT * FROM game_records ORDER BY playedAt DESC")
    fun getAllRecords(): Flow<List<GameRecordEntity>>

    @Query("DELETE FROM game_records")
    suspend fun clearAll()
}