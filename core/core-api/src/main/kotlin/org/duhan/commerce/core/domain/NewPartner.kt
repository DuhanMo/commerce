package org.duhan.commerce.core.domain

data class NewPartner(
    val email: String,
    val password: String,
    val businessName: String,
    val businessNumber: String,
    val brand: NewBrand,
) {
    data class NewBrand(
        val name: String,
        val description: String,
    )
}
