package com.reminder.application

import com.reminder.core.model.note.Note
import com.reminder.core.model.note.NoteRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class NoteManager(
    private val noteRepository: NoteRepository
) {

    fun getNotes(date: Date, userId: Long): List<Note> {
        return noteRepository.getNotes(date, userId)
    }

    fun removeNote(id: Long) {
        return noteRepository.deleteNote(id)
    }

    fun updateOrCreateNote(note: Note, userId: Long): Note {
        return noteRepository.saveNote(note, userId)
    }
}