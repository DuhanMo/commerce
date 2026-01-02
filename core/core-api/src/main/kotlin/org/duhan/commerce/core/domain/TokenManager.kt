package org.duhan.commerce.core.domain

import org.duhan.commerce.storage.db.core.RefreshTokenEntity
import org.duhan.commerce.storage.db.core.RefreshTokenRepository
import org.duhan.commerce.support.jwt.JwtProperties
import org.duhan.commerce.support.jwt.TokenProvider
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TokenManager(
    private val tokenProvider: TokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProperties: JwtProperties,
) {
    fun generateToken(accountId: Long, email: String, role: String): AuthToken {
        val accessToken = tokenProvider.createAccessToken(accountId, email, role)
        val refreshToken = tokenProvider.createRefreshToken(accountId)

        saveOrUpdateRefreshToken(accountId, refreshToken)

        return AuthToken(accessToken, refreshToken)
    }

    private fun saveOrUpdateRefreshToken(accountId: Long, token: String) {
        val expiryDate = Instant.now().plusSeconds(jwtProperties.refreshTokenExpiration)

        val refreshToken = refreshTokenRepository.findByAccountId(accountId)?.apply {
            updateToken(token, jwtProperties.refreshTokenExpiration)
        } ?: RefreshTokenEntity(
            accountId = accountId,
            token = token,
            expiryDate = expiryDate
        )

        refreshTokenRepository.save(refreshToken)
    }
}
