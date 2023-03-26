package com.bizarre.core_data.repository


import com.bizarre.core_data.datasource.reminder.ReminderDataSource
import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.repository.ReminderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDataSource.addReminder(reminder)
    }



    override suspend fun loadAllReminders(): List<Reminder> {
        return reminderDataSource.loadAllReminders()
    }
}