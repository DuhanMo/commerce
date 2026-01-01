package org.duhan.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.duhan.commerce.core.enums.UserRole

@Entity
@Table(name = "account")
class AccountEntity(
    val email: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: UserRole,
) : BaseEntity()
