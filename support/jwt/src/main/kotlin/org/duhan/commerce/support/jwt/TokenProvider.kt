package org.duhan.commerce.support.jwt

interface TokenProvider {
    fun createToken(accountId: Long, email: String, role: String): String
    fun getSubject(token: String): String // Email
    fun getAccountId(token: String): Long // Account ID
    fun getRole(token: String): String // Role
    fun validateToken(token: String): Boolean
}
