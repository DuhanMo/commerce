package org.duhan.commerce.core.domain

import java.math.BigDecimal

data class NewProduct(
    val brandId: Long,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val originPrice: BigDecimal,
    val sellingPrice: BigDecimal,
    val defaultStock: Long,
    val optionGroups: List<OptionGroup>,
) {
    data class OptionGroup(
        val name: String,
        val isEssential: Boolean,
        val ordering: Int,
        val optionValues: List<OptionValue>,
    )

    data class OptionValue(
        val name: String,
        val ordering: Int,
        val extraPrice: BigDecimal,
    )
}
