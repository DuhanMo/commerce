package org.duhan.commerce.core.domain

import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.storage.db.core.PartnerRepository
import org.springframework.stereotype.Component

@Component
class PartnerValidator(
    private val partnerRepository: PartnerRepository,
) {
    fun validateBusinessNumber(businessNumber: String) {
        if (partnerRepository.existsByBusinessNumber(businessNumber)) {
            throw CoreException(ErrorType.INVALID_REQUEST, "존재하는 사업자 번호입니다")
        }
    }
}
