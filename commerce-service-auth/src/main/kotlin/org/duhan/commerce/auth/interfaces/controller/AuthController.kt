package org.duhan.commerce.auth.interfaces.controller

import org.duhan.commerce.auth.application.AuthApplicationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authApplicationService: AuthApplicationService,
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): TokenResponse {
        val result = authApplicationService.login(request.toCommand())
        return TokenResponse.from(result)
    }
}
