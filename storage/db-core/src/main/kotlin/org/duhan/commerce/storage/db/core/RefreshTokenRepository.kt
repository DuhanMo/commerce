package org.duhan.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByAccountId(accountId: Long): RefreshTokenEntity?
}
