package org.duhan.commerce.auth.interfaces.event

import org.duhan.commerce.auth.application.CreateAccountCommand
import org.duhan.commerce.auth.domain.UserRole
import java.util.UUID

data class UserCreatedEvent(
    val userId: UUID,
    val email: String,
    val password: String,
    val role: UserRole,
) {
    fun toCommand(): CreateAccountCommand = CreateAccountCommand(
        userId = userId,
        email = email,
        password = password,
        role = role,
    )
}
