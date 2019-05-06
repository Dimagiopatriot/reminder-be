package com.reminder.application

import com.reminder.core.model.note.Note
import com.reminder.core.model.note.NoteRepository
import com.reminder.core.model.user.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.*

@Component
class NoteManager(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) {

    fun getNotes(date: Date, userName: String): List<Note> {
        val userId = userRepository.getUserByUserName(userName).id
        return noteRepository.getNotes(date, userId)
    }

    fun removeNote(id: Long) {
        return noteRepository.deleteNote(id)
    }

    fun updateOrCreateNote(note: Note, userName: String): Note {
        val userId = userRepository.getUserByUserName(userName).id
        return noteRepository.saveNote(note, userId)
    }
}