package org.duhan.commerce.core.domain

import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.storage.db.core.AccountEntity
import org.duhan.commerce.storage.db.core.AccountRepository
import org.springframework.stereotype.Component

@Component
class AccountReader(
    private val accountRepository: AccountRepository,
) {
    fun readByEmail(email: String): AccountEntity {
        return accountRepository.findByEmail(email)
            ?: throw CoreException(ErrorType.INVALID_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다.")
    }

    fun readById(id: Long): AccountEntity {
        return accountRepository.findById(id).orElseThrow {
            CoreException(ErrorType.NOT_FOUND_DATA, "계정을 찾을 수 없습니다.")
        }
    }
}
