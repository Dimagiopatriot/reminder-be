package com.reminder.core.model.note

import java.util.*

interface NoteRepository {

    fun getNotes(date: Date): List<Note>
    fun updateNote(note: Note): Note
    fun deleteNote(id: Long)
    fun createNote(note: Note): Note
}