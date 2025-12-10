package org.duhan.commerce.core.api.controller.v1.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.duhan.commerce.core.domain.NewProduct
import java.math.BigDecimal

data class RegisterProductRequest(
    @field:NotNull(message = "브랜드 식별자는 필수입니다.")
    val brandId: Long?,
    @field:NotBlank(message = "상품명은 필수입니다.")
    val name: String,
    @field:NotBlank(message = "상품 설명은 필수입니다.")
    val description: String,
    @field:NotBlank(message = "썸네일 URL은 필수입니다.")
    val thumbnailUrl: String,
    @field:Positive(message = "원가는 양수여야 합니다.")
    val originPrice: BigDecimal?,
    @field:Positive(message = "판매가는 양수여야 합니다.")
    val sellingPrice: BigDecimal?,
    @field:NotNull(message = "기본 재고는 필수입니다.")
    @field:PositiveOrZero(message = "재고는 0개 이상이어야 합니다.")
    val defaultStock: Long?,
    @field:NotNull(message = "옵션 그룹 목록은 필수입니다(없으면 빈 리스트 []).")
    @field:Valid
    val optionGroups: List<OptionGroupRequest>?,
) {
    fun toNewProduct(): NewProduct {
        return NewProduct(
            brandId = brandId!!,
            name = name,
            description = description,
            thumbnailUrl = thumbnailUrl,
            originPrice = originPrice!!,
            sellingPrice = sellingPrice!!,
            defaultStock = defaultStock!!,
            optionGroups = optionGroups?.map { it.toOptionGroup() } ?: emptyList(),
        )
    }

    data class OptionGroupRequest(
        @field:NotBlank(message = "옵션 그룹명은 필수입니다.")
        val name: String,
        @field:NotNull(message = "필수 옵션 여부는 필수입니다.")
        val isEssential: Boolean?,
        @field:NotNull(message = "정렬 순서는 필수입니다.")
        val ordering: Int?,
        @field:NotEmpty(message = "옵션 값은 최소 1개 이상 등록해야 합니다.")
        @field:Valid
        val optionValues: List<OptionValueRequest>?,
    ) {
        fun toOptionGroup(): NewProduct.OptionGroup {
            return NewProduct.OptionGroup(
                name = name,
                isEssential = isEssential!!,
                ordering = ordering!!,
                optionValues = optionValues!!.map { it.toOptionValue() },
            )
        }
    }

    data class OptionValueRequest(
        @field:NotBlank(message = "옵션값 이름은 필수입니다.")
        val name: String,
        @field:NotNull(message = "정렬 순서는 필수입니다.")
        val ordering: Int?,
        @field:PositiveOrZero(message = "추가 금액은 0원 이상이어야 합니다.")
        val extraPrice: BigDecimal?,
    ) {
        fun toOptionValue(): NewProduct.OptionValue {
            return NewProduct.OptionValue(
                name = name,
                ordering = ordering!!,
                extraPrice = extraPrice!!,
            )
        }
    }
}