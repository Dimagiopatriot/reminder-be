package com.reminder.api

import com.reminder.api.Routing.Companion.LOGIN_API
import com.reminder.application.UserManager
import com.reminder.infrastructure.config.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
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
    private val userManager: UserManager,
    val authenticationManager: AuthenticationManager,
    val tokenProvider: JwtTokenProvider
) {

    @PostMapping
    fun doPost(@Valid @RequestBody loginBody: LoginBody): ResponseEntity<TokenDto> {
        userManager.getUserFromDatabase(loginBody.email, loginBody.password)?.let {
            val authentication =
                authenticationManager.authenticate(UsernamePasswordAuthenticationToken(it.userName, it.password))
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = tokenProvider.generateToken((authentication.principal as UserDetails).username)

            return ResponseEntity.ok(TokenDto(null, jwt))
        } ?: return ResponseEntity.ok(TokenDto("No user in DB", null))
    }
}

data class LoginBody(
    @Email
    @NotBlank
    val email: String,
    @NotBlank
    val password: String
)

data class TokenDto(val error: String?, val accessToken: String?)