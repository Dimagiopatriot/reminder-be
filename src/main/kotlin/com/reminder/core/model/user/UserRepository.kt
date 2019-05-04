package com.reminder.core.model.user

interface UserRepository {
    fun createUser(user: User): User
    fun getUser(email: String, password: String): User
}