package com.example.travelapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.travelapp.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Upsert
    suspend fun upsert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("select * from user u where u.id = :id")
    suspend fun findById(id:Int):User

    @Query("select * from user u where u.user = :user and u.password = :password")
    suspend fun findByUserAndPassword(user:String,password:String):User
    @Query("Select * from user u")
    fun findAll(): Flow<List<User>>

}