package com.bizarre.core_domain.repository

import com.bizarre.core_domain.entity.Reminder


interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder)
    suspend fun loadAllReminders(): List<Reminder>
}