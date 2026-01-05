package org.duhan.commerce.auth.interfaces.controller

import org.duhan.commerce.auth.application.TokenResult

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun from(result: TokenResult): TokenResponse = TokenResponse(
            result.accessToken,
            result.refreshToken,
        )
    }
}
