package com.bizarre.assistedreminderapp.ui.reminder

import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User

sealed interface ReminderState {
    object Loading : ReminderState
    data class Error(val throwable: Throwable) : ReminderState
    data class Success(
        val selectedReminder: Reminder?,
        val data: List<Reminder>
    ) : ReminderState
}