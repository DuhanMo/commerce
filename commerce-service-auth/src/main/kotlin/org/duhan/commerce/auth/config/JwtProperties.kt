package org.duhan.commerce.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secretKey: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long,
)
