package org.duhan.commerce.auth.application

data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
)
