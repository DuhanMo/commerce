package org.duhan.commerce.auth.domain

import java.util.UUID

interface TokenProvider {
    fun createAccessToken(userId: UUID, email: String, role: String): String
    fun createRefreshToken(userId: UUID): String
}
