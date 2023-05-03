package com.bizarre.core_database.dao

import androidx.room.*
import com.bizarre.core_database.entity.ReminderEntity
import com.bizarre.core_domain.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: ReminderEntity): Long

    @Update(entity = ReminderEntity::class)
    suspend fun update(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders")
    fun findAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE reminderId LIKE :reminderId")
    fun findReminderById(reminderId: Long): ReminderEntity

    @Query("SELECT * FROM reminders WHERE user_id LIKE :userId")
    fun findRemindersByUser(userId: Long): Flow<List<ReminderEntity>>


    @Delete
    suspend fun delete(reminder: ReminderEntity)
}