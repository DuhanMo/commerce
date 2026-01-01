package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.duhan.commerce.core.enums.PartnerState

@Entity
@Table(name = "partner")
class PartnerEntity(
    val accountId: Long,
    val businessName: String,
    val businessNumber: String,
    @Enumerated(EnumType.STRING)
    val state: PartnerState = PartnerState.PENDING,
) : BaseEntity()
