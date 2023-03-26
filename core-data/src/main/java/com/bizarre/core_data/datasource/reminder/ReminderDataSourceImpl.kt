package com.bizarre.core_data.datasource.reminder

import com.bizarre.core_database.dao.ReminderDao
import com.bizarre.core_database.entity.ReminderEntity

import com.bizarre.core_domain.entity.Reminder
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class ReminderDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderDataSource {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())


    }

    override suspend fun loadAllReminders():List<Reminder> {
        return reminderDao.findAll().map {
            it.fromEntity()
        }
    }

    private fun Reminder.toEntity() = ReminderEntity(



        reminderId = this.reminderId,
          message = this.message,
        location_x = this.location_x,
       location_y = this.location_y,
      reminder_date =  this.reminder_date,
   creation_date = this.creation_date,
    creator_email =  creator_email,
     is_seen = this.is_seen

    )

    private fun ReminderEntity.fromEntity() = Reminder(
        reminderId = this.reminderId,
        message = this.message,
        location_x = this.location_x,
        location_y = this.location_y,
        reminder_date =  this.reminder_date,
        creation_date = this.creation_date,
        creator_email =  creator_email,
        is_seen = this.is_seen
    )
}