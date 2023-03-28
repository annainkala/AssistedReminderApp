package com.bizarre.core_domain.entity

data class User(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val password: String,
    val userName: String,
    val profilePic: String,
    val userEmail: String,

)