package org.duhan.commerce.core.domain

import org.assertj.core.api.Assertions.assertThat
import org.duhan.commerce.ContextTest
import org.duhan.commerce.storage.db.core.InventoryRepository
import org.duhan.commerce.storage.db.core.OptionGroupRepository
import org.duhan.commerce.storage.db.core.OptionValueRepository
import org.duhan.commerce.storage.db.core.ProductRepository
import org.duhan.commerce.storage.db.core.ProductVariantOptionValueRepository
import org.duhan.commerce.storage.db.core.ProductVariantRepository
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductServiceTest(
    private val productService: ProductService,
    private val productRepository: ProductRepository,
    private val optionGroupRepository: OptionGroupRepository,
    private val optionValueRepository: OptionValueRepository,
    private val productVariantRepository: ProductVariantRepository,
    private val productVariantOptionValueRepository: ProductVariantOptionValueRepository,
    private val inventoryRepository: InventoryRepository,
) : ContextTest() {
    @Test
    fun `상품 등록시 옵션 조합 수만큼 상품 배리언트가 생성된다`() {
        // given
        val newProduct = NewProduct(
            brandId = 1L,
            name = "나이키 티셔츠",
            description = "빠른 건조, 쾌적한 착용감",
            thumbnailUrl = "http://thumbnail.com",
            originPrice = BigDecimal("2000"),
            sellingPrice = BigDecimal("1000"),
            defaultStock = 100L,
            optionGroups = listOf(
                NewProduct.OptionGroup(
                    name = "컬러",
                    isEssential = true,
                    ordering = 1,
                    optionValues = listOf(
                        NewProduct.OptionValue(
                            name = "화이트",
                            ordering = 1,
                            extraPrice = BigDecimal.ZERO,
                        ),
                        NewProduct.OptionValue(
                            name = "블랙",
                            ordering = 2,
                            extraPrice = BigDecimal("2000"),
                        ),
                    ),
                ),
                NewProduct.OptionGroup(
                    name = "사이즈",
                    isEssential = true,
                    ordering = 1,
                    optionValues = listOf(
                        NewProduct.OptionValue(
                            name = "M",
                            ordering = 1,
                            extraPrice = BigDecimal.ZERO,
                        ),
                        NewProduct.OptionValue(
                            name = "L",
                            ordering = 2,
                            extraPrice = BigDecimal("1000"),
                        ),
                        NewProduct.OptionValue(
                            name = "XL",
                            ordering = 3,
                            extraPrice = BigDecimal("2000"),
                        ),
                    ),
                ),
            ),
        )

        // when
        productService.registerProduct(newProduct)

        // then
        assertThat(productRepository.findAll().size).isEqualTo(1)
        // 옵션 그룹 [컬러, 사이즈]
        assertThat(optionGroupRepository.findAll().size).isEqualTo(2)
        // 옵션 값 [화이트, 블랙, M, L, XL]
        assertThat(optionValueRepository.findAll().size).isEqualTo(5)
        // 상품 배리언트 [화이트+M, 화이트+L, 화이트+XL, 블랙+M, 블랙+L, 블랙+XL]
        assertThat(productVariantRepository.findAll().size).isEqualTo(6)
        // 재고는 배리언트 수와 동일
        assertThat(inventoryRepository.findAll().size).isEqualTo(6)
        // 상품 배리언트 옵션 매핑 [6배리언트 * 2옵션 = 12]
        assertThat(productVariantOptionValueRepository.findAll().size).isEqualTo(12)
    }
}
