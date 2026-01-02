package org.duhan.commerce.core.domain

import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordVerifier(
    private val passwordEncoder: PasswordEncoder,
) {
    fun verify(rawPassword: String, encodedPassword: String) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw CoreException(ErrorType.INVALID_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다.")
        }
    }
}
