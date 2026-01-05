package org.duhan.commerce.auth.interfaces.event

import org.duhan.commerce.auth.application.AuthService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AuthListener(
    private val authService: AuthService,
) {
    @KafkaListener(topics = ["user-created"], groupId = "auth")
    fun listen(event: UserCreatedEvent) {
        authService.createAccount(event.toCommand())
    }
}
