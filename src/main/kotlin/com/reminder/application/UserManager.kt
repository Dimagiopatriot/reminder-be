package com.reminder.application

import com.reminder.core.model.user.User
import com.reminder.core.model.user.UserRepository
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepository: UserRepository
) {

    fun getUserFromDatabase(email: String, password: String): User {
        return userRepository.getUser(email, password)
    }

    fun createUser(userName: String, email: String, password: String): User {
        return userRepository.createUser(User(userName, email, password))
    }
}