package com.reminder.core.model.note

import com.reminder.core.model.Status

data class Note(
    val id: Long?,
    val timestamp: Long,
    val noteName: String,
    val description: String?,
    val status: Status
)