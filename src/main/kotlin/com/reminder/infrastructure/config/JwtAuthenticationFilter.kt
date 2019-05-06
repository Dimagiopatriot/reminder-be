package com.reminder.infrastructure.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.persistence.EntityNotFoundException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    val tokenProvider: JwtTokenProvider,
    @Qualifier("userService")
    @Lazy val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    companion object {
        const val TOKEN_TYPE: String = "Bearer"
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.getHeader("Authorization")?.let {
            try {
                val jwt = it.substringAfter(TOKEN_TYPE, "")

                if (tokenProvider.validateToken(jwt)) {
                    val subject = tokenProvider.getSubjectFromJWT(jwt)
                    val userDetails = userDetailsService.loadUserByUsername(subject)
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (ex: EntityNotFoundException) {
                log.warn("JTW is provided with incorrect userId: ${ex.message}")
            } catch (ex: Exception) {
                log.error("Could not set user authentication in security context", ex)
            }
        }

        filterChain.doFilter(request, response)
    }
}