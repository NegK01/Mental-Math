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

    // Usado exclusivamente para calcular métricas globales en HistoryViewModel.
    // Nunca se usa para renderizar la lista visible — eso va por getRecordsPaged.
    @Query("SELECT * FROM game_records ORDER BY playedAt DESC")
    fun getAllRecords(): Flow<List<GameRecordEntity>>

    // Paginación manual — LIMIT controla el tamaño de página, OFFSET la posición.
    // suspend (no Flow) porque la carga es imperativa: el ViewModel la llama
    // explícitamente en loadMore(), no reactivamente.
    @Query("SELECT * FROM game_records ORDER BY playedAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getRecordsPaged(limit: Int, offset: Int): List<GameRecordEntity>

    @Query("DELETE FROM game_records")
    suspend fun clearAll()

    @Query("SELECT MAX(CAST(correctAnswers AS REAL) / totalRounds) FROM game_records WHERE difficulty = :difficulty")
    suspend fun getBestAccuracyForDifficulty(difficulty: String): Double?
}