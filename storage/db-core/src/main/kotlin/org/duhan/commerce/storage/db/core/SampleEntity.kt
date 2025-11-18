package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.duhan.commerce.core.enums.SampleState

@Entity
class SampleEntity(
    val name: String,
    @Enumerated(EnumType.STRING)
    val state: SampleState = SampleState.NOT_SALE,
) : BaseEntity()
