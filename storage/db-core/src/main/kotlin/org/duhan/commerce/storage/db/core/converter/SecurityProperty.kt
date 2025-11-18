package org.duhan.commerce.storage.db.core.converter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "duhan.storage.core.security")
internal data class SecurityProperty(
    val key: String,
    val iv: String,
)
