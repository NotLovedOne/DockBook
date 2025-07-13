package com.example.doctorbooking.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    var secret: String = "T0pS3cr3tKeyForJwtThatIsAtLeast32Chars"
    var expiration: Long = 86400000 // 24h
} 