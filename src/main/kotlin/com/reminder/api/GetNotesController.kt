package com.reminder.api

import com.reminder.api.Routing.Companion.GET_NOTES
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat

@RestController
@RequestMapping(GET_NOTES)
class GetNotesController {

    @GetMapping
    fun doFet(@RequestParam date: String) {
        val sdf = SimpleDateFormat(date)
    }
}