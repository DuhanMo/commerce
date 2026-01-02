package org.duhan.commerce.core.domain

import org.duhan.commerce.core.enums.UserRole
import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.storage.db.core.AccountRepository
import org.duhan.commerce.support.jwt.TokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val accountValidator: AccountValidator,
    private val partnerValidator: PartnerValidator,
    private val accountWriter: AccountWriter,
    private val partnerWriter: PartnerWriter,

    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
) {
    @Transactional
    fun signUpPartner(newPartner: NewPartner): Long {
        accountValidator.validateEmail(newPartner.email)
        partnerValidator.validateBusinessNumber(newPartner.businessNumber)

        val account = accountWriter.createAccount(newPartner.email, newPartner.password)
        val partner = partnerWriter.createPartner(account.id, newPartner)

        return partner.id
    }

    fun loginPartner(login: LoginAction): String {
        // 1. 계정 조회
        val account = accountRepository.findByEmail(login.email)
            ?: throw CoreException(ErrorType.INVALID_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다.")

        // 2. 파트너 권한 확인 (중요!)
        if (account.role != UserRole.ROLE_PARTNER) {
            throw CoreException(ErrorType.INVALID_REQUEST, "파트너 전용 로그인입니다.")
        }

        // 3. 비밀번호 검증
        if (!passwordEncoder.matches(login.password, account.password)) {
            throw CoreException(ErrorType.INVALID_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다.")
        }

        // 4. 토큰 생성 (ID, 이메일, 권한 포함)
        return tokenProvider.createToken(
            accountId = account.id,
            email = account.email,
            role = account.role.name,
        )
    }
}
