package com.negk01.mentalmath.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "mental_math_db"
            )
                .addMigrations(AppDatabase.MIGRATION_3_4)
                // No existen migraciones para v1→v2 ni v2→v3. Sin este fallback,
                // usuarios en esas versiones crashean con IllegalStateException.
                // Explícito por versión: v3→v4 usa MIGRATION_3_4 sin fallback.
                .fallbackToDestructiveMigrationFrom(1, 2)
                .build()

            INSTANCE = instance
            instance
        }
    }
}
