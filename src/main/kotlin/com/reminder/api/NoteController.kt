package com.reminder.api

import com.reminder.api.Routing.Companion.CREATE_NOTE
import com.reminder.api.Routing.Companion.DELETE_NOTE
import com.reminder.api.Routing.Companion.UPDATE_NOTE
import org.springframework.web.bind.annotation.*

@RestController
class NoteController {

    @PatchMapping(UPDATE_NOTE)
    fun doPatch(@RequestBody userRequestBody: UserRequestBody) {
    }

    @DeleteMapping(DELETE_NOTE)
    fun doDelete(@RequestParam id: Long) {
    }

    @PutMapping(CREATE_NOTE)
    fun doPut(@RequestBody userRequestBody: UserRequestBody) {
    }
}

data class UserRequestBody(
    val id: Long?,
    val timestamp: Long,
    val noteName: String,
    val description: String?,
    val status: String
)