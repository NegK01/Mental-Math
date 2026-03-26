package com.negk01.mentalmath.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.negk01.mentalmath.data.local.dao.GameRecordDao
import com.negk01.mentalmath.data.local.dao.SettingsDao
import com.negk01.mentalmath.data.local.entity.GameRecordEntity
import com.negk01.mentalmath.data.local.entity.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        GameRecordEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun gameRecordDao(): GameRecordDao
}
