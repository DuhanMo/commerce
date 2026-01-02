package org.duhan.commerce.core.api.controller.v1.request

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
