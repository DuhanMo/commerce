package org.duhan.commerce.core.domain

import org.duhan.commerce.core.enums.UserRole
import org.duhan.commerce.storage.db.core.AccountEntity
import org.duhan.commerce.storage.db.core.AccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AccountWriter(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createAccount(email: String, password: String): AccountEntity {
        return AccountEntity(
            email = email,
            password = passwordEncoder.encode(password),
            role = UserRole.ROLE_PARTNER,
        ).let { accountRepository.save(it) }
    }
}
