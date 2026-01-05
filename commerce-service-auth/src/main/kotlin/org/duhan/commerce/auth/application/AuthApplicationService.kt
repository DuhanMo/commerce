package org.duhan.commerce.auth.application

import org.duhan.commerce.auth.domain.AccountRepository
import org.duhan.commerce.auth.domain.TokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthApplicationService(
    private val passwordEncoder: PasswordEncoder,
    private val accountRepository: AccountRepository,
    private val tokenProvider: TokenProvider,
) {
    fun login(command: LoginCommand): TokenResult {
        val account = accountRepository.findByEmail(command.email)
            ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")
        if (account.email == null || account.password == null) {
            throw IllegalArgumentException("잘못된 로그인 방식입니다.")
        }
        if (!passwordEncoder.matches(command.password, account.password)) {
            throw IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.")
        }

        return TokenResult(
            tokenProvider.createAccessToken(account.userId, account.email!!, account.role.name),
            tokenProvider.createRefreshToken(account.userId),
        )
    }
}
