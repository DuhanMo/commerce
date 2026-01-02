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
    private val accountReader: AccountReader,
    private val passwordVerifier: PasswordVerifier,
    private val tokenManager: TokenManager,
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
        val account = accountReader.readByEmail(login.email)

        if (account.role != UserRole.ROLE_PARTNER) {
            throw CoreException(ErrorType.INVALID_REQUEST, "파트너 전용 로그인입니다.")
        }

        passwordVerifier.verify(login.password, account.password)

        return tokenManager.generateToken(account.id, account.email, account.role.name)
    }
}
