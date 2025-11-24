package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "product_variant_option_value")
class ProductVariantOptionValueEntity(
    val productVariantId: Long,
    val optionValueId: Long,
) : BaseEntity()
