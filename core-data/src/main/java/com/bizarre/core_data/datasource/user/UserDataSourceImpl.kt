package com.bizarre.core_data.datasource.user


import android.util.Log
import com.bizarre.core_database.dao.UserDao
import com.bizarre.core_database.entity.UserEntity


import com.bizarre.core_domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserDataSource {

    override suspend fun addUser(user: User):Long{
        Log.d("","INSERTTTTTTTT2222222222222::::: " + user.toString())
       return userDao.insertOrUpdate(user.toEntity())
    }

    override suspend fun deleteUser(user: User) {
        userDao.delete(user.userId)
    }

    override suspend fun loadUsers(): Flow<List<User>> = flow {
        emit(
            userDao.findAll().map {
                it.fromEntity()
            }
        )
//        val data = fakeData
//        emit(data)
    }

 

    private fun User.toEntity() = UserEntity(
        userId = this.userId,
    firstName =  this.firstName,
    lastName = this.lastName,
    password = this.password,
    userName = this.userName,
     profilePic = this.profilePic,
    userEmail = this.userEmail,


    )

    private fun UserEntity.fromEntity() = User(
        userId = this.userId,
        firstName = this.firstName,
        lastName = this.lastName,
        password = this.password,
        userName = this.userName,
        profilePic = this.profilePic,
        userEmail = this.userEmail,


    )
}