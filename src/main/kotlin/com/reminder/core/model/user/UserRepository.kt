package com.reminder.core.model.user

interface UserRepository {
    fun createUser(userName: String, email: String, password: String): User
    fun getUser(email: String, password: String): User
    fun getUserByUserName(userName: String): User
}