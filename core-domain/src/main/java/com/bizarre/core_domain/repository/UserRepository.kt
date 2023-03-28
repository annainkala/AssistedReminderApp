package com.bizarre.core_domain.repository

import com.bizarre.core_domain.entity.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun loadUsers(): Flow<List<User>>


}