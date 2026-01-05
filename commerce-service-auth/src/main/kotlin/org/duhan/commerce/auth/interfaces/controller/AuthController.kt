package org.duhan.commerce.auth.interfaces.controller

import com.fasterxml.uuid.Generators
import org.duhan.commerce.auth.application.AuthService
import org.duhan.commerce.auth.domain.UserRole
import org.duhan.commerce.auth.interfaces.event.UserCreatedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): TokenResponse {
        val result = authService.login(request.toCommand())
        return TokenResponse.from(result)
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest) {
        kafkaTemplate.send(
            "user-created",
            UserCreatedEvent(
                userId = Generators.timeBasedEpochGenerator().generate(),
                email = request.email,
                password = request.password,
                role = request.role,
            ),
        )
    }

    data class SignUpRequest(
        val email: String,
        val password: String,
        val role: UserRole,
    )
}
