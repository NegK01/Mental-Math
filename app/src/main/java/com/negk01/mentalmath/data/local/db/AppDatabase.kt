package com.negk01.mentalmath.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.negk01.mentalmath.data.local.dao.GameRecordDao
import com.negk01.mentalmath.data.local.dao.SettingsDao
import com.negk01.mentalmath.data.local.entity.GameRecordEntity
import com.negk01.mentalmath.data.local.entity.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        GameRecordEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun gameRecordDao(): GameRecordDao

    companion object {

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN hasSeenOnboarding INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN hasDismissedHomeSupportCard INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
