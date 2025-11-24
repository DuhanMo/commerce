package org.duhan.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface ProductPriceRepository : JpaRepository<ProductPriceEntity, Long>
