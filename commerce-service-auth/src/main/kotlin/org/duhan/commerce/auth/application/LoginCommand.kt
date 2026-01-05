package org.duhan.commerce.auth.application

data class LoginCommand(
    val email: String,
    val password: String,
)
