package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "option_group")
class OptionGroupEntity(
    val productId: Long,
    val name: String,
    val isEssential: Boolean,
    val ordering: Int,
) : BaseEntity()
