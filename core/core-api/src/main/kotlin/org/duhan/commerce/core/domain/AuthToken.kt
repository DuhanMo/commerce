package org.duhan.commerce.core.domain

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)
