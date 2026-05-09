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
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun gameRecordDao(): GameRecordDao

    companion object {

        // ── GUÍA DE MIGRACIONES ────────────────────────────────────────────────────
        // Room compara la versión declarada en @Database con la del archivo en disco.
        // Si difieren y no existe una ruta de migration definida, .fallbackToDestructiveMigration()
        // borra y recrea toda la BD — los datos del usuario se pierden.
        //
        // CÓMO AÑADIR UN NUEVO CAMPO (ejemplo: añadir `foo` a la tabla `settings`):
        //   1. Añadir el campo en SettingsEntity con un valor default:
        //        val foo: String = ""
        //   2. Añadir el campo en AppSettings (domain model) con el mismo default.
        //   3. Actualizar SettingsMapper (toDomain y toEntity) para mapear el campo.
        //   4. Incrementar `version` en @Database (ej. 4 → 5).
        //   5. Declarar una nueva migration aquí:
        //        val MIGRATION_4_5 = object : Migration(4, 5) {
        //            override fun migrate(db: SupportSQLiteDatabase) {
        //                db.execSQL("ALTER TABLE settings ADD COLUMN foo TEXT NOT NULL DEFAULT ''")
        //            }
        //        }
        //   6. Registrar la migration en DatabaseProvider:
        //        .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
        //
        // REGLAS CLAVE:
        //   • ALTER TABLE solo puede AÑADIR columnas, nunca eliminarlas ni renombrarlas en SQLite.
        //     Para esos casos se necesita recrear la tabla con un nombre temporal, migrar datos,
        //     drop tabla original y renombrar la temporal.
        //   • Siempre usar DEFAULT en el ALTER para que las filas existentes tengan valor válido.
        //   • Room encadena migrations automáticamente: si el usuario está en v3 y la app
        //     es v5, Room aplica MIGRATION_3_4 → MIGRATION_4_5 en orden.
        //   • INTEGER NOT NULL DEFAULT 0  →  Boolean false en Kotlin/Room
        //   • INTEGER NOT NULL DEFAULT 1  →  Boolean true
        //   • TEXT NOT NULL DEFAULT ''    →  String vacío
        // ──────────────────────────────────────────────────────────────────────────

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN hasSeenOnboarding INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
