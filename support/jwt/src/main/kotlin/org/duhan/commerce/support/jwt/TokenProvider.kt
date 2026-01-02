package org.duhan.commerce.support.jwt

interface TokenProvider {
    fun createAccessToken(accountId: Long, email: String, role: String): String
    fun createRefreshToken(accountId: Long): String
    fun getSubject(token: String): String // Email
    fun getAccountId(token: String): Long // Account ID
    fun getRole(token: String): String // Role
    fun validateToken(token: String): Boolean
}
