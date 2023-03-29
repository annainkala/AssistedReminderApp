package com.bizarre.core_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bizarre.core_database.entity.ReminderEntity
import com.bizarre.core_database.entity.UserEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE userId LIKE :userId")
    fun findOne(userId: Long): Flow<UserEntity>

    @Query("SELECT * FROM users")
    suspend fun findAll(): List<UserEntity>

    @Query("DELETE FROM users WHERE userId LIKE :userId")
    suspend fun delete(userId: Long)
 
}