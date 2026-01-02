package org.duhan.commerce.core.domain

import org.duhan.commerce.core.enums.UserRole
import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.storage.db.core.AccountRepository
import org.duhan.commerce.storage.db.core.RefreshTokenEntity
import org.duhan.commerce.storage.db.core.RefreshTokenRepository
import org.duhan.commerce.support.jwt.JwtProperties
import org.duhan.commerce.support.jwt.TokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthService(
    private val accountValidator: AccountValidator,
    private val partnerValidator: PartnerValidator,
    private val accountWriter: AccountWriter,
    private val partnerWriter: PartnerWriter,

    private val jwtProperties: JwtProperties,
    private val accountRepository: AccountRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
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

    fun loginPartner(login: LoginAction): AuthToken {
        // 1. 계정 조회
        val account = accountRepository.findByEmail(login.email)
            ?: throw CoreException(ErrorType.INVALID_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다.")

        // 2. 파트너 권한 확인
        if (account.role != UserRole.ROLE_PARTNER) {
            throw CoreException(ErrorType.INVALID_REQUEST, "파트너 전용 로그인입니다.")
        }

        // 3. 비밀번호 검증
        if (!passwordEncoder.matches(login.password, account.password)) {
            throw CoreException(ErrorType.INVALID_REQUEST, "아이디 또는 비밀번호가 잘못되었습니다.")
        }
        val accessToken = tokenProvider.createAccessToken(account.id, account.email, account.role.name)
        val refreshTokenString = tokenProvider.createRefreshToken(account.id)

        saveOrUpdateRefreshToken(account.id, refreshTokenString)

        // 4. 토큰 생성 (ID, 이메일, 권한 포함)
        return AuthToken(accessToken, refreshTokenString)
    }

    private fun saveOrUpdateRefreshToken(accountId: Long, token: String) {
        val expiryDate = Instant.now().plusSeconds(jwtProperties.refreshTokenExpiration)

        val refreshToken = refreshTokenRepository.findByAccountId(accountId)?.apply {
            updateToken(token, jwtProperties.refreshTokenExpiration)
        } ?: RefreshTokenEntity(
            accountId = accountId,
            token = token,
            expiryDate = expiryDate,
        )

        refreshTokenRepository.save(refreshToken)
    }
}
