package org.duhan.commerce.support.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val properties: JwtProperties,
) : TokenProvider {
    private val key: SecretKey = Keys.hmacShaKeyFor(
        properties.secretKey.toByteArray(StandardCharsets.UTF_8),
    )

    override fun createToken(accountId: Long, email: String, role: String): String {
        val now = Date()
        val validity = Date(now.time + properties.accessTokenExpiration * 1000)

        return Jwts.builder()
            .subject(email) // "sub" 클레임
            .claim("id", accountId) // 커스텀 클레임 (ID)
            .claim("role", role) // 커스텀 클레임 (Role)
            .issuedAt(now)
            .expiration(validity)
            .signWith(key)
            .compact()
    }

    override fun getSubject(token: String): String = getClaims(token).payload.subject

    override fun getAccountId(token: String): Long = getClaims(token).payload["id"].toString().toLong()

    override fun getRole(token: String): String = getClaims(token).payload["role"].toString()

    override fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            true
        } catch (e: JwtException) {
            // Expired, Malformed, SignatureException 등을 모두 포함
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    // 💡 최신 parser 문법: parserBuilder() 대신 parser() + verifyWith() 사용
    private fun getClaims(token: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
    }
}
