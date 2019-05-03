package com.reminder.api

import com.reminder.api.Routing.Companion.LOGIN_API
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(LOGIN_API)
class LoginController {

    @PostMapping
    fun doPost(@Valid @RequestBody loginDto: LoginDto) {

    }
}

data class LoginDto(
    @Email
    @NotBlank
    val email: String,
    @NotBlank
    val password: String
)