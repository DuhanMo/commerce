package org.duhan.commerce.core.domain

import org.duhan.commerce.storage.db.core.OptionValueEntity
import org.duhan.commerce.storage.db.core.ProductEntity
import org.duhan.commerce.storage.db.core.ProductVariantEntity
import org.duhan.commerce.storage.db.core.ProductVariantOptionValueEntity
import org.duhan.commerce.storage.db.core.ProductVariantOptionValueRepository
import org.duhan.commerce.storage.db.core.ProductVariantRepository
import org.springframework.stereotype.Component

@Component
class ProductVariantProcessor(
    private val productVariantRepository: ProductVariantRepository,
    private val productVariantOptionValueRepository: ProductVariantOptionValueRepository,
) {
    fun createVariants(
        product: ProductEntity,
        optionValueLists: List<List<OptionValueEntity>>,
    ): List<ProductVariantEntity> {
        val combinations = cartesianProduct(optionValueLists)
        // 배리언트 저장
        val variants = combinations.map { combination ->
            val variantName = if (combination.isEmpty()) {
                product.name
            } else {
                "${product.name} - ${combination.joinToString(" / ") { it.name }}"
            }
            val finalExtraPrice = combination.sumOf { it.extraPrice }
            ProductVariantEntity(
                productId = product.id,
                name = variantName,
                finalExtraPrice = finalExtraPrice,
            )
        }
        val savedVariants = productVariantRepository.saveAll(variants)
        val mappings = combinations.zip(savedVariants).flatMap { (combination, variant) ->
            combination.map { ov ->
                    ProductVariantOptionValueEntity(
                        productVariantId = variant.id,
                        optionValueId = ov.id,
                    )
            }
        }
        // 배리언트 옵션 매핑 저장
        productVariantOptionValueRepository.saveBulk(mappings)

        return savedVariants
    }


    /**
     * [[화이트, 블랙],[M, L]] -> [[화이트, M],[화이트, L],[블랙, M],[블랙, L]]
     */
    private fun cartesianProduct(lists: List<List<OptionValueEntity>>): List<List<OptionValueEntity>> {
        return lists.fold(listOf(listOf())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }
    }
}