package com.bizarre.core_data


import com.bizarre.core_data.datasource.reminder.ReminderDataSource
import com.bizarre.core_data.datasource.reminder.ReminderDataSourceImpl
import com.bizarre.core_data.datasource.user.UserDataSource
import com.bizarre.core_data.datasource.user.UserDataSourceImpl
import com.bizarre.core_data.repository.ReminderRepositoryImpl
import com.bizarre.core_data.repository.UserRepositoryImpl
import com.bizarre.core_domain.entity.User
import com.bizarre.core_domain.repository.ReminderRepository
import com.bizarre.core_domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindReminderDataSource(
        reminderDataSource: ReminderDataSourceImpl
    ): ReminderDataSource

    @Singleton
    @Binds
    fun bindReminderRepository(
        reminderRepository: ReminderRepositoryImpl
    ): ReminderRepository

    @Singleton
    @Binds
    fun bindUserDataSource(
        userDataSource: UserDataSourceImpl
    ): UserDataSource

    @Singleton
    @Binds
    fun bindUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository



}