package org.duhan.commerce.core.domain

import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.storage.db.core.AccountRepository
import org.springframework.stereotype.Component

@Component
class AccountValidator(
    private val accountRepository: AccountRepository,
) {
    fun validateEmail(email: String) {
        if (accountRepository.findByEmail(email) != null) {
            throw CoreException(ErrorType.INVALID_REQUEST, "중복된 이메일입니다")
        }
    }
}
