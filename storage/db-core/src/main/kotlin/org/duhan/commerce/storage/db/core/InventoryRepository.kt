package org.duhan.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface InventoryRepository : JpaRepository<InventoryEntity, Long>
