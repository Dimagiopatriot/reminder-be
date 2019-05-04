package com.reminder.api

import com.reminder.api.Routing.Companion.CREATE_NOTE
import com.reminder.api.Routing.Companion.DELETE_NOTE
import com.reminder.api.Routing.Companion.UPDATE_NOTE
import com.reminder.application.NoteManager
import com.reminder.core.model.Status
import com.reminder.core.model.note.Note
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class NoteController(
    private val noteManager: NoteManager
) {

    @PatchMapping(UPDATE_NOTE)
    fun doPatch(@RequestBody noteBody: NoteBody): ResponseEntity<String> {
        //todo add real userId
        noteManager.updateOrCreateNote(
            Note(
                noteBody.id,
                noteBody.timestamp,
                noteBody.noteName,
                noteBody.description,
                Status.valueOf(noteBody.status)
            ),
            121212
        )
        return ResponseEntity.ok("status: updated")
    }

    @DeleteMapping(DELETE_NOTE)
    fun doDelete(@RequestParam id: Long): ResponseEntity<String> {
        noteManager.removeNote(id)
        return ResponseEntity.ok("status: deleted")
    }

    @PutMapping(CREATE_NOTE)
    fun doPut(@RequestBody noteBody: NoteBody): ResponseEntity<NoteDto> {
        //todo add real userId
        val note = noteManager.updateOrCreateNote(
            Note(
                noteBody.id,
                noteBody.timestamp,
                noteBody.noteName,
                noteBody.description,
                Status.valueOf(noteBody.status)
            ),
            121212
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