package com.reminder.api

import com.reminder.api.Routing.Companion.REGISTER_API
import com.reminder.application.UserManager
import com.reminder.infrastructure.config.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(REGISTER_API)
class RegisterController(
    private val userManager: UserManager,
    val authenticationManager: AuthenticationManager,
    val tokenProvider: JwtTokenProvider
) {

    @PostMapping
    fun doPost(@Valid @RequestBody registerBody: RegisterBody): ResponseEntity<RegisterResponseDto> {
        val user = userManager.createUser(registerBody.userName, registerBody.email, registerBody.password)
        val authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.email, user.password))
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.generateToken((authentication.principal as UserDetails).username)
        //todo add accessToken
        return ResponseEntity.ok(
            RegisterResponseDto(
                user = UserDto(user.userName, user.email),
                accessToken = "accessToken: $jwt"
            )
        )
    }
}

data class RegisterBody(
    @NotBlank
    val userName: String,
    @Email
    @NotBlank
    val email: String,
    @NotBlank
    val password: String
)

data class RegisterResponseDto(
    val user: UserDto,
    val accessToken: String
) : Serializable

data class UserDto(val userName: String, val email: String) : Serializable