package com.reminder.core.model.note

import java.util.*

interface NoteRepository {

    fun getNotes(date: Date, userId: Long): List<Note>
    fun saveNote(note: Note, userId: Long): Note
    fun deleteNote(id: Long)
}