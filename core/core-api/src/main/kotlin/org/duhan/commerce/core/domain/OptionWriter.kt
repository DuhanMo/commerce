package org.duhan.commerce.core.domain

import org.duhan.commerce.storage.db.core.OptionGroupEntity
import org.duhan.commerce.storage.db.core.OptionGroupRepository
import org.duhan.commerce.storage.db.core.OptionValueEntity
import org.duhan.commerce.storage.db.core.OptionValueRepository
import org.springframework.stereotype.Component

@Component
class OptionWriter(
    private val optionGroupRepository: OptionGroupRepository,
    private val optionValueRepository: OptionValueRepository,
) {
    fun createOptions(productId: Long, newProduct: NewProduct): List<List<OptionValueEntity>> {
        val optionValueLists = newProduct.optionGroups.map { og ->
            val optionGroup = optionGroupRepository.save(
                OptionGroupEntity(
                    productId = productId,
                    name = og.name,
                    isEssential = og.isEssential,
                    ordering = og.ordering,
                ),
            )
            val optionValues = og.optionValues.map { ov ->
                OptionValueEntity(
                    optionGroupId = optionGroup.id,
                    name = ov.name,
                    ordering = ov.ordering,
                    extraPrice = ov.extraPrice,
                )
            }
            optionValueRepository.saveAll(optionValues)
        }
        return optionValueLists
    }
}