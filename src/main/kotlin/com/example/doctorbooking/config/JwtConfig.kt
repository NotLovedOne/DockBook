package com.example.doctorbooking.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    @Value("\${jwt.secret}")
    var secret: String? = null
    var expiration: Long = 86400000 // 24h
} 