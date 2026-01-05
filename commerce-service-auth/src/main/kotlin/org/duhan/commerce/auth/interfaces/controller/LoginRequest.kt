package org.duhan.commerce.auth.interfaces.controller

import org.duhan.commerce.auth.application.LoginCommand

data class LoginRequest(
    val email: String,
    val password: String,
) {
    fun toCommand(): LoginCommand = LoginCommand(email, password)
}
