package com.negk01.mentalmath.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.negk01.mentalmath.data.local.dao.GameRecordDao
import com.negk01.mentalmath.data.local.dao.ReminderProgressDao
import com.negk01.mentalmath.data.local.dao.SettingsDao
import com.negk01.mentalmath.data.local.entity.GameRecordEntity
import com.negk01.mentalmath.data.local.entity.ReminderProgressEntity
import com.negk01.mentalmath.data.local.entity.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        GameRecordEntity::class,
        ReminderProgressEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun gameRecordDao(): GameRecordDao
    abstract fun reminderProgressDao(): ReminderProgressDao

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

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN notificationsEnabled INTEGER NOT NULL DEFAULT 0")
                db.execSQL("CREATE TABLE IF NOT EXISTS reminder_progress (id INTEGER NOT NULL PRIMARY KEY, lastPlayedAt INTEGER NOT NULL, stagePriority INTEGER NOT NULL)")
            }
        }
    }
}
