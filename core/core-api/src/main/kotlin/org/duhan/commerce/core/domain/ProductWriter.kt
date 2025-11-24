package org.duhan.commerce.core.domain

import org.duhan.commerce.storage.db.core.ProductEntity
import org.duhan.commerce.storage.db.core.ProductPriceEntity
import org.duhan.commerce.storage.db.core.ProductPriceRepository
import org.duhan.commerce.storage.db.core.ProductRepository
import org.springframework.stereotype.Component

@Component
class ProductWriter(
    private val productRepository: ProductRepository,
    private val productPriceRepository: ProductPriceRepository,
) {
    fun createProduct(newProduct: NewProduct): ProductEntity {
        val product = productRepository.save(
            ProductEntity(
                brandId = newProduct.brandId,
                name = newProduct.name,
                description = newProduct.description,
                thumbnailUrl = newProduct.thumbnailUrl,
            ),
        )

        productPriceRepository.save(
            ProductPriceEntity(
                productId = product.id,
                originalPrice = newProduct.originPrice,
                sellingPrice = newProduct.sellingPrice,
            ),
        )

        return product
    }
}