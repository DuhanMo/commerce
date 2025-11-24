package org.duhan.commerce.storage.db.core

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement

interface ProductVariantOptionValueCustomRepository {
    fun saveBulk(productVariantOptionValues: List<ProductVariantOptionValueEntity>)
}

@Repository
class ProductVariantOptionValueCustomRepositoryImpl(
    private val coreJdbcTemplate: JdbcTemplate
): ProductVariantOptionValueCustomRepository {
    override fun saveBulk(productVariantOptionValues: List<ProductVariantOptionValueEntity>) {
        if (productVariantOptionValues.isEmpty()) return

        val sql = """
            INSERT INTO product_variant_option_value 
            (product_variant_id, option_value_id) 
            VALUES (?, ?)
        """.trimIndent()

        coreJdbcTemplate.batchUpdate(
            sql,
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val entity = productVariantOptionValues[i]
                    ps.setLong(1, entity.productVariantId)
                    ps.setLong(2, entity.optionValueId)
                }

                override fun getBatchSize(): Int {
                    return productVariantOptionValues.size
                }
            }
        )
    }

}
