package com.example.doctorbooking.util

import com.example.doctorbooking.config.JwtConfig
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(private val jwtConfig: JwtConfig) {

    private val secretKey = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val expiry = Date(now.time + jwtConfig.expiration)

        return Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateToken(email: String, role: String): String {
        val now = Date()
        val expiry = Date(now.time + jwtConfig.expiration)

        return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername(token: String): String? = try {
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    } catch (e: Exception) {
        null
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean = try {
        val expiration = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .expiration
        expiration.before(Date())
    } catch (e: Exception) {
        true
    }
}
