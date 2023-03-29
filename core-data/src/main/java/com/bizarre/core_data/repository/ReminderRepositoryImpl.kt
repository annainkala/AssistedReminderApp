package com.bizarre.core_data.repository


import com.bizarre.core_data.datasource.reminder.ReminderDataSource
import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User
import com.bizarre.core_domain.repository.ReminderRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {
    override suspend fun updateReminder(reminder: Reminder) {
        reminderDataSource.updateReminder(reminder)
    }

    override suspend fun addReminder(reminder: Reminder):Long   = reminderDataSource.addReminder(reminder)

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDataSource.deleteReminder(reminder)
    }


    override suspend fun loadRemindersByUser(user: User): Flow<List<Reminder>>{
        return reminderDataSource.loadRemindersByUser(user)
    }

    override suspend fun loadAllReminders(): List<Reminder> {
        return reminderDataSource.loadAllReminders()
    }
}