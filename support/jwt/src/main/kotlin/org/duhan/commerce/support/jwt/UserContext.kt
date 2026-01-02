package org.duhan.commerce.support.jwt

// SecurityContext의 Principal 에 담길 객체
data class UserContext(
    val id: Long,
    val email: String,
    val role: String,
)
