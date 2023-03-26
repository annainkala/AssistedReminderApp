package com.bizarre.core_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    indices = [
        Index("reminderId", unique = true)

    ],


)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0,
    val message: String,
    val location_x: Double,
    val location_y: Double,
    val reminder_date: LocalDateTime,
    val creation_date: LocalDateTime,
    val creator_email: String,
    val is_seen: Boolean = false)


