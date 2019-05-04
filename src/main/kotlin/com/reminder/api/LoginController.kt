package com.reminder.api

import com.reminder.api.Routing.Companion.LOGIN_API
import com.reminder.application.UserManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(LOGIN_API)
class LoginController(
    private val userManager: UserManager
) {

    @PostMapping
    fun doPost(@Valid @RequestBody loginBody: LoginBody): ResponseEntity<String> {
        val user = userManager.getUserFromDatabase(loginBody.email, loginBody.password)
        //todo add token here
        return ResponseEntity.ok(
            "accessToken: "
        )
    }
}

data class LoginBody(
    @Email
    @NotBlank
    val email: String,
    @NotBlank
    val password: String
)