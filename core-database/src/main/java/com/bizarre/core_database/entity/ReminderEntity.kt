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
        Index("reminderId", unique = true),
        Index("user_id")

    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)


data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0,
    val message: String,
    val title: String,
    val location_x: Double,
    val location_y: Double,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val reminder_date: LocalDateTime,
    val creation_date: LocalDateTime,
    val is_seen: Boolean = false)


