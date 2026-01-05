package org.duhan.commerce.auth.domain

interface AccountRepository {
    fun findByEmail(email: String): Account?
}
