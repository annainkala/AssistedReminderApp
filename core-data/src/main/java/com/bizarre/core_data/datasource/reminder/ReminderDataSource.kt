package com.bizarre.core_data.datasource.reminder

import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User

import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    suspend fun findReminderById(id:Long):Reminder
    suspend fun addReminder(reminder: Reminder): Long
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun loadAllReminders(): Flow<List<Reminder>>
    suspend fun loadRemindersByUser(user: User): Flow<List<Reminder>>
}