package com.reminder.infrastructure.config

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class JwtTokenProvider(
    @Value("\${jwt.jwtSecret}") private val jwtSecret: String,
    @Value("\${jwt.jwtExpirationInH}") private val jwtExpirationInH: Long
) {

    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    fun generateToken(subject: String): String {
        val now = Date()
        val jwtExpirationInMs = TimeUnit.HOURS.toMillis(jwtExpirationInH)
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getSubjectFromJWT(token: String): String {
        val claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            log.warn("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            log.warn("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            log.warn("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            log.warn("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            log.warn("JWT claims string is empty.")
        }

        return false
    }
}