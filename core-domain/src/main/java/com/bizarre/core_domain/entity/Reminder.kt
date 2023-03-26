package com.bizarre.core_domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val message: String,
    val location_x: Double,
    val location_y: Double,
    val reminder_date: LocalDateTime,
    val creation_date: LocalDateTime,
    val creator_email: String,
    val is_seen: Boolean = false)
