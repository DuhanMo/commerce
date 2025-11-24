package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "inventory")
class InventoryEntity(
    val productVariantId: Long,
    val quantity: Long,
) : BaseEntity()
