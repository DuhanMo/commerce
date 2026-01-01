package org.duhan.commerce.core.api.controller.v1

import jakarta.validation.Valid
import org.duhan.commerce.core.api.controller.v1.request.PartnerSignUpRequest
import org.duhan.commerce.core.domain.PartnerService
import org.duhan.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val partnerService: PartnerService,
) {
    @PostMapping("/v1/auth/signup/partner")
    fun signUpPartner(
        @RequestBody @Valid request: PartnerSignUpRequest,
    ): ApiResponse<Any> {
        partnerService.signUp(request.toNewPartner())
        return ApiResponse.success()
    }
}
