package com.example.travelapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.travelapp.entity.Travel
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {

    @Insert
    suspend fun insert(travel: Travel)

    @Update
    suspend fun update(travel: Travel)

    @Upsert
    suspend fun upsert(travel: Travel)

    @Delete
    suspend fun delete(travel: Travel)

    @Query("SELECT * FROM travel WHERE id = :id")
    suspend fun findById(id: Int): Travel?

    @Query("Select * from travel t")
    fun findAll(): Flow<List<Travel>>

    @Query("UPDATE travel SET script = :script WHERE id = :travelId")
    suspend fun updateSuggestion(travelId: Int, script: String)
}