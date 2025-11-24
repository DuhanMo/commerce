package org.duhan.commerce.core.domain

import org.duhan.commerce.storage.db.core.InventoryEntity
import org.duhan.commerce.storage.db.core.InventoryRepository
import org.duhan.commerce.storage.db.core.ProductVariantEntity
import org.springframework.stereotype.Component

@Component
class InventoryWriter(
    private val inventoryRepository: InventoryRepository,
) {
    fun createInventories(variants: List<ProductVariantEntity>, defaultStock: Long) {
        inventoryRepository.saveAll(variants.map { variant ->
            InventoryEntity(
                productVariantId = variant.id,
                quantity = defaultStock,
            )
        })
    }
}