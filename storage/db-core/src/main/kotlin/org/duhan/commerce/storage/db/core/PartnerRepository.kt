package org.duhan.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface PartnerRepository : JpaRepository<PartnerEntity, Long> {
    fun existsByBusinessNumber(businessNumber: String): Boolean
}
