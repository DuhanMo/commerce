package org.duhan.commerce.core.api.controller.v1.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.duhan.commerce.core.domain.LoginAction

data class LoginRequest(
    @field:Email
    val email: String,
    @field:NotBlank
    @field:Size(min = 8, max = 20)
    val password: String,
) {
    fun toAction(): LoginAction = LoginAction(email, password)
}
