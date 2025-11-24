package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product_variant")
class ProductVariantEntity(
    val productId: Long,
    val name: String,
    val finalExtraPrice: BigDecimal,
) : BaseEntity()
