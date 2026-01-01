package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "brand")
class BrandEntity(
    val name: String,
    val description: String,
) : BaseEntity()
