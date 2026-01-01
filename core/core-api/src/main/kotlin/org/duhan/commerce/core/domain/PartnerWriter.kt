package org.duhan.commerce.core.domain

import org.duhan.commerce.storage.db.core.BrandEntity
import org.duhan.commerce.storage.db.core.BrandRepository
import org.duhan.commerce.storage.db.core.PartnerEntity
import org.duhan.commerce.storage.db.core.PartnerRepository
import org.springframework.stereotype.Component

@Component
class PartnerWriter(
    private val partnerRepository: PartnerRepository,
    private val brandRepository: BrandRepository,
) {
    fun createPartner(accountId: Long, newPartner: NewPartner): PartnerEntity {
        val partner = PartnerEntity(
            accountId = accountId,
            businessName = newPartner.businessName,
            businessNumber = newPartner.businessNumber,
        ).let { partnerRepository.save(it) }
        BrandEntity(
            name = newPartner.brand.name,
            description = newPartner.brand.description,
        ).let { brandRepository.save(it) }

        return partner
    }
}
