package org.duhan.commerce.core.api.controller.v1

import jakarta.validation.Valid
import org.duhan.commerce.core.api.controller.v1.request.RegisterProductRequest
import org.duhan.commerce.core.domain.ProductService
import org.duhan.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService,
) {
    @PostMapping("/v1/products")
    fun registerProduct(
        @RequestBody @Valid request: RegisterProductRequest,
    ): ApiResponse<Any> {
        productService.registerProduct(request.toNewProduct())
        return ApiResponse.success()
    }
}

