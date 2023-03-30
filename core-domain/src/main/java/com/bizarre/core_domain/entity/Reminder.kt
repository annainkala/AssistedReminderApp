package com.bizarre.core_domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    var userId: Long = 0,
    var message: String,
    val location_x: Double,
    val location_y: Double,
    var reminder_date: LocalDateTime,
    var creation_date: LocalDateTime,
    val is_seen: Boolean = false)
