package com.bizarre.core_database.dao

import androidx.room.*
import com.bizarre.core_database.entity.ReminderEntity
import com.bizarre.core_database.entity.UserEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM users WHERE userId LIKE :userId")
    fun findReminderById(userId: Long): Flow<UserEntity>

    @Query("SELECT * FROM users")
    suspend fun findAll(): List<UserEntity>

    @Query("DELETE FROM users WHERE userId LIKE :userId")
    suspend fun delete(userId: Long)
 
}