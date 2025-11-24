package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product_price")
class ProductPriceEntity(
    val productId: Long,
    val originalPrice: BigDecimal,
    val sellingPrice: BigDecimal,
) : BaseEntity()
