package org.duhan.commerce.core.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productWriter: ProductWriter,
    private val optionWriter: OptionWriter,
    private val variantProcessor: ProductVariantProcessor,
    private val inventoryWriter: InventoryWriter,
) {
    @Transactional
    fun registerProduct(newProduct: NewProduct): Long {
        val product = productWriter.createProduct(newProduct)
        val optionValueLists = optionWriter.createOptions(product.id, newProduct)
        val variants = variantProcessor.createVariants(product, optionValueLists)
        inventoryWriter.createInventories(variants, newProduct.defaultStock)
        return product.id
    }
}
