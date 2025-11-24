package org.duhan.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface ProductVariantRepository : JpaRepository<ProductVariantEntity, Long>
