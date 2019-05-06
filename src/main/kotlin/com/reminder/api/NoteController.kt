package com.reminder.api

import com.reminder.api.Routing.Companion.CREATE_NOTE
import com.reminder.api.Routing.Companion.DELETE_NOTE
import com.reminder.api.Routing.Companion.UPDATE_NOTE
import com.reminder.application.NoteManager
import com.reminder.core.model.Status
import com.reminder.core.model.note.Note
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
class NoteController(
    private val noteManager: NoteManager
) {

    @PatchMapping(UPDATE_NOTE, produces = [MediaType.APPLICATION_JSON_VALUE])
    fun doPatch(@RequestBody noteBody: NoteBody): String {
        noteBody.id?.let {
            val userName = (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
            noteManager.updateOrCreateNote(
                Note(
                    noteBody.id,
                    noteBody.timestamp,
                    noteBody.noteName,
                    noteBody.description,
                    Status.valueOf(noteBody.status.toUpperCase())
                ),
                userName
            )
            return "{ \"status\": \"updated\" }"
        } ?: return "{ \"status\": \"failed. No id\" }"
    }

    @DeleteMapping(DELETE_NOTE, produces = [MediaType.APPLICATION_JSON_VALUE])
    fun doDelete(@RequestParam id: Long): String {
        noteManager.removeNote(id)
        return "{ \"status\": \"deleted\" }"
    }

    @PutMapping(CREATE_NOTE)
    fun doPut(@RequestBody noteBody: NoteBody): ResponseEntity<NoteDto> {
        val userName = (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
        val note = noteManager.updateOrCreateNote(
            Note(
                noteBody.id,
                noteBody.timestamp,
                noteBody.noteName,
                noteBody.description,
                Status.valueOf(noteBody.status.toUpperCase())
            ),
            userName
        )
        return ResponseEntity.ok(NoteDto(note.id!!, note.timestamp, note.noteName, note.description, note.status.name))
    }
}

data class NoteBody(
    val id: Long?,
    val timestamp: Long,
    val noteName: String,
    val description: String?,
    val status: String
)