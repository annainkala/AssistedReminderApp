package com.bizarre.core_data.datasource.reminder

import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User

import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    suspend fun addReminder(reminder: Reminder): Long

    suspend fun deleteReminder(reminder: Reminder)
    suspend fun loadAllReminders(): List<Reminder>
    suspend fun loadRemindersByUser(user: User): Flow<List<Reminder>>
}