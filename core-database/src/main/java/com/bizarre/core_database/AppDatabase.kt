package com.bizarre.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bizarre.core_database.dao.ReminderDao
import com.bizarre.core_database.dao.UserDao
import com.bizarre.core_database.entity.ReminderEntity
import com.bizarre.core_database.entity.UserEntity
import com.bizarre.core_database.utils.LocalDateTimeConverter

@Database(
    entities = [ReminderEntity::class,UserEntity::class],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun userDao(): UserDao
}