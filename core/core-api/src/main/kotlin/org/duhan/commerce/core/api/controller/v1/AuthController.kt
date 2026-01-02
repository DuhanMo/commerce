package org.duhan.commerce.core.api.controller.v1

import jakarta.validation.Valid
import org.duhan.commerce.core.api.controller.v1.request.LoginRequest
import org.duhan.commerce.core.api.controller.v1.request.PartnerSignUpRequest
import org.duhan.commerce.core.api.controller.v1.request.TokenResponse
import org.duhan.commerce.core.domain.AuthService
import org.duhan.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/v1/auth/signup/partner")
    fun signUpPartner(
        @RequestBody @Valid request: PartnerSignUpRequest,
    ): ApiResponse<Any> {
        authService.signUpPartner(request.toNewPartner())
        return ApiResponse.success()
    }

    @PostMapping("/v1/auth/login/partner")
    fun loginPartner(@RequestBody request: LoginRequest): TokenResponse {
        val token = authService.loginPartner(request.toAction())
        return TokenResponse(token)
    }
}
