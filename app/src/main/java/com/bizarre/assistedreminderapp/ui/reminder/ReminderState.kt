package com.bizarre.assistedreminderapp.ui.reminder

import com.bizarre.core_domain.entity.Reminder


interface ReminderState {

    object Loading: ReminderState
    data class Success(
        val data: List<Reminder>
    ): ReminderState
}
