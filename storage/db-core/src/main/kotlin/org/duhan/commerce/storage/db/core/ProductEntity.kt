package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.duhan.commerce.core.enums.ProductState

@Entity
@Table(name = "product")
class ProductEntity(
    val brandId: Long,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    @Enumerated(EnumType.STRING)
    val state: ProductState = ProductState.PREPARED,
) : BaseEntity()
