package com.bizarre.assistedreminderapp.ui.user

import com.bizarre.core_domain.entity.User


sealed interface UserState {
    object Loading : UserState
    data class Error(val throwable: Throwable) : UserState
    data class Success(
        val selectedUser: User?,
        val data: List<User>
    ) : UserState
}