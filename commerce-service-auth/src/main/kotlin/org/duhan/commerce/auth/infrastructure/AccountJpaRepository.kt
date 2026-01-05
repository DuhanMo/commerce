package org.duhan.commerce.auth.infrastructure

import org.duhan.commerce.auth.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?
}
