package org.duhan.commerce.core.api.controller.v1.request

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.duhan.commerce.core.domain.NewPartner

data class PartnerSignUpRequest(
    @field:Email
    val email: String,
    @field:NotBlank
    @field:Size(min = 8, max = 20)
    val password: String,
    @field:NotBlank
    val businessName: String,
    @field:Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 번호 형식이 올바르지 않습니다.")
    val businessNumber: String,
    @field:Valid
    val brand: BrandRequest,
) {
    fun toNewPartner(): NewPartner {
        return NewPartner(
            email = email,
            password = password,
            businessName = businessName,
            businessNumber = businessNumber,
            brand = NewPartner.NewBrand(
                name = brand.name,
                description = brand.description,
            ),
        )
    }

    data class BrandRequest(
        @field:NotBlank
        val name: String,
        @field:NotBlank
        val description: String,
    )
}
