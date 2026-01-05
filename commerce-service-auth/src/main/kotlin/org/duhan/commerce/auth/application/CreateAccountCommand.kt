package org.duhan.commerce.auth.application

import org.duhan.commerce.auth.domain.UserRole
import java.util.UUID

data class CreateAccountCommand(
    val userId: UUID,
    val email: String,
    val password: String,
    val role: UserRole,
)
