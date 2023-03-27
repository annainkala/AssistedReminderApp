package com.bizarre.core_data.repository


import com.bizarre.core_data.datasource.reminder.ReminderDataSource
import com.bizarre.core_data.datasource.user.UserDataSource
import com.bizarre.core_domain.entity.Reminder
import com.bizarre.core_domain.entity.User
import com.bizarre.core_domain.repository.ReminderRepository
import com.bizarre.core_domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun addUser(user: User): Long = userDataSource.addUser(user)
    override suspend fun deleteUser(user: User): Long = userDataSource.addUser(user)
    override suspend fun loadUsers(): Flow<List<User>> {
        return userDataSource.loadUsers()
    }

}