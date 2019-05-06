package com.reminder.application

import com.reminder.core.model.user.User
import com.reminder.core.model.user.UserRepository
import com.reminder.infrastructure.mysql.CustomSQLException
import com.reminder.infrastructure.mysql.NoSuchElementInDbException
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepository: UserRepository
) {

    fun getUserFromDatabase(email: String, password: String): User? {
        return try {
            userRepository.getUser(email, password)
        } catch (e: NoSuchElementInDbException) {
            null
        }
    }

    @Throws(CustomSQLException::class)
    fun createUser(userName: String, email: String, password: String): User {
        return userRepository.createUser(User(userName, email, password))
    }
}