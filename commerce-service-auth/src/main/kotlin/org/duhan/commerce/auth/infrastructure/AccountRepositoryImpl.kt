package org.duhan.commerce.auth.infrastructure

import org.duhan.commerce.auth.domain.Account
import org.duhan.commerce.auth.domain.AccountRepository
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryImpl(
    private val jpaRepository: AccountJpaRepository,
) : AccountRepository {
    override fun findByEmail(email: String): Account? = jpaRepository.findByEmail(email)
}
