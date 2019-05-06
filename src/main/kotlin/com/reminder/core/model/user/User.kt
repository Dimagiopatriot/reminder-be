package com.reminder.core.model.user

data class User (
    val id: Long,
    val userName: String,
    val email: String,
    val password: String
)