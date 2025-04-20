package com.example.travelapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
                Room.databaseBuilder(context, AppDatabase::class.java, "user_database").build()
                    .also { Instance = it }
            }
        }
    }
}
