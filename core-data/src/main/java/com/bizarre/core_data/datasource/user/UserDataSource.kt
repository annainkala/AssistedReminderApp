package com.bizarre.core_data.datasource.user


import com.bizarre.core_domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun updateUser(user: User)
    suspend fun addUser(user: User):Long
    suspend fun deleteUser(user: User)
    suspend fun loadUsers(): Flow<List<User>>
}