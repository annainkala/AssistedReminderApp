package com.bizarre.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bizarre.core_database.dao.ReminderDao
import com.bizarre.core_database.entity.ReminderEntity
import com.bizarre.core_database.utils.LocalDateTimeConverter

@Database(
    entities = [ReminderEntity::class],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}