package com.bizarre.core_data.datasource.reminder

import com.bizarre.core_domain.entity.Reminder

import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    suspend fun addReminder(reminder: Reminder)


    suspend fun loadAllReminders(): List<Reminder>
}