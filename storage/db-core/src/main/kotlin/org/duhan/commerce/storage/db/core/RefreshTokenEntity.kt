package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "refresh_token")
class RefreshTokenEntity(
    val accountId: Long,
    var token: String,
    var expiryDate: Instant,
) : BaseEntity() {
    fun updateToken(newToken: String, durationSeconds: Long) {
        this.token = newToken
        this.expiryDate = Instant.now().plusSeconds(durationSeconds)
    }
}
