package org.duhan.commerce.auth.infrastructure

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.duhan.commerce.auth.config.JwtProperties
import org.duhan.commerce.auth.domain.TokenProvider
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val properties: JwtProperties,
) : TokenProvider {
    private val key: SecretKey = Keys.hmacShaKeyFor(
        properties.secretKey.toByteArray(StandardCharsets.UTF_8),
    )

    override fun createAccessToken(userId: UUID, email: String, role: String): String {
        return createToken(userId, email, role, properties.accessTokenExpiration)
    }

    override fun createRefreshToken(userId: UUID): String {
        return createToken(userId, null, null, properties.refreshTokenExpiration)
    }

    private fun createToken(userId: UUID, email: String?, role: String?, expiration: Long): String {
        val now = Date()
        val validity = Date(now.time + expiration * 1000)

        return Jwts.builder()
            .apply {
                email?.let { subject(it) }
                role?.let { claim("role", it) }
            }
            .claim("id", userId)
            .issuedAt(now)
            .expiration(validity)
            .signWith(key)
            .compact()
    }
}
