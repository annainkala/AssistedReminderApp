package com.bizarre.assistedreminderapp.ui.reminder

import com.bizarre.core_domain.entity.Reminder


interface ReminderViewState {

    object Loading: ReminderViewState
    data class Success(
        val data: List<Reminder>
    ): ReminderViewState
}
