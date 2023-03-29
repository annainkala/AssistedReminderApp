package com.bizarre.core_domain.repository

import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User
import kotlinx.coroutines.flow.Flow


interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder):Long
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun loadRemindersByUser(userId: User):Flow<List<Reminder>>
    suspend fun loadAllReminders(): List<Reminder>
}