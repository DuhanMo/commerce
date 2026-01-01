package org.duhan.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<BrandEntity, Long>
