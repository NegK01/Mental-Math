package com.negk01.mentalmath.data.mapper

import com.negk01.mentalmath.data.local.entity.GameRecordEntity
import com.negk01.mentalmath.domain.model.GameRecord

fun GameRecordEntity.toDomain(): GameRecord {
    return GameRecord(
        id = id,
        playedAt = playedAt,
        difficulty = difficulty,
        correctAnswers = correctAnswers,
        totalRounds = totalRounds,
        averageResponseTimeMillis = averageResponseTimeMillis,
        maxStreak = maxStreak
    )
}

fun GameRecord.toEntity(): GameRecordEntity {
    return GameRecordEntity(
        id = id,
        playedAt = playedAt,
        difficulty = difficulty,
        correctAnswers = correctAnswers,
        totalRounds = totalRounds,
        averageResponseTimeMillis = averageResponseTimeMillis,
        maxStreak = maxStreak
    )
}