package com.bizarre.core_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "users",
    indices = [
        Index("userId", unique = true)

    ],


    )
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val firstName: String,
    val lastName: String,
    val password: String,
    val userName: String,
    val profilePic: String,
    val userEmail: String,)

