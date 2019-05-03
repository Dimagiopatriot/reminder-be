package com.reminder.api

import com.reminder.api.Routing.Companion.REGISTER_API
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(REGISTER_API)
class RegisterController {

    @PostMapping
    fun doPost(@Valid @RequestBody registerDto: RegisterDto) {

    }
}

data class RegisterDto(
    @NotBlank
    val userName: String,
    @Email
    @NotBlank
    val email: String,
    @NotBlank
    val password: String
)