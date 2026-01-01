package org.duhan.commerce.core.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PartnerService(
    private val accountValidator: AccountValidator,
    private val partnerValidator: PartnerValidator,
    private val accountWriter: AccountWriter,
    private val partnerWriter: PartnerWriter,
) {
    @Transactional
    fun signUp(newPartner: NewPartner): Long {
        accountValidator.validateEmail(newPartner.email)
        partnerValidator.validateBusinessNumber(newPartner.businessNumber)

        val account = accountWriter.createAccount(newPartner.email, newPartner.password)
        val partner = partnerWriter.createPartner(account.id, newPartner)

        return partner.id
    }
}
