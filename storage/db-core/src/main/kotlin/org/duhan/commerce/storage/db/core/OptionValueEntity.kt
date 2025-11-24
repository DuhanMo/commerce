package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "option_value")
class OptionValueEntity(
    val optionGroupId: Long,
    val name: String,
    val ordering: Int,
    val extraPrice: BigDecimal,
) : BaseEntity()
