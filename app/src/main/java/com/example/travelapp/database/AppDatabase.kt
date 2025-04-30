package com.example.travelapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.travelapp.dao.TravelDao
import com.example.travelapp.dao.UserDao
import com.example.travelapp.entity.Travel
import com.example.travelapp.entity.User


@Database(
    entities = [User::class, Travel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun travelDao(): TravelDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "user_database")
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE `travel` " +
                    "ADD COLUMN IF NOT EXISTS `startDate` DATE NOT NULL DEFAULT CURRENT_DATE," +
                    "ADD COLUMN IF NOT EXISTS `endDate` DATE NOT NULL DEFAULT CURRENT_DATE"
        )
    }
}