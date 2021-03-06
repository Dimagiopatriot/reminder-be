package com.reminder.api

import com.reminder.api.Routing.Companion.GET_NOTES
import com.reminder.application.NoteManager
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.text.SimpleDateFormat

@RestController
@RequestMapping(GET_NOTES)
class GetNotesController(
    private val noteManager: NoteManager
) {

    @GetMapping
    fun doGet(@RequestParam date: String): ResponseEntity<List<NoteDto>> {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val userName = (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
        val notes = noteManager.getNotes(sdf.parse(date), userName)
        val notesDto = notes.map {
            NoteDto(it.id!!, it.timestamp, it.noteName, it.description, it.status.name)
        }
        return ResponseEntity.ok(notesDto)
    }
}

data class NoteDto(
    val id: Long,
    val timestamp: Long,
    val noteName: String,
    val description: String?,
    val status: String
) : Serializable