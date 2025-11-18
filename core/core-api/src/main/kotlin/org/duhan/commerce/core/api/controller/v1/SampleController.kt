package org.duhan.commerce.core.api.controller.v1

import org.duhan.commerce.core.api.controller.v1.request.CreateSampleRequest
import org.duhan.commerce.core.api.controller.v1.response.SampleResponse
import org.duhan.commerce.core.domain.SampleService
import org.duhan.commerce.core.support.OffsetLimit
import org.duhan.commerce.core.support.response.ApiResponse
import org.duhan.commerce.core.support.response.PageResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController(
    private val sampleService: SampleService,
) {
    @GetMapping("/v1/samples")
    fun getSamples(
        @RequestParam offset: Int,
        @RequestParam limit: Int,
    ): ApiResponse<PageResponse<SampleResponse>> {
        val result = sampleService.findSamples(OffsetLimit(offset, limit))
        return ApiResponse.success(PageResponse(SampleResponse.of(result.content), result.hasNext))
    }

    @GetMapping("/v1/samples/{sampleId}")
    fun getSample(
        @PathVariable sampleId: Long,
    ): ApiResponse<SampleResponse> {
        val sample = sampleService.findSample(sampleId)
        return ApiResponse.success(SampleResponse.of(sample))
    }

    @PostMapping("/v1/samples")
    fun createSample(
        @RequestBody request: CreateSampleRequest,
    ): ApiResponse<Any> {
        sampleService.createSample(request.toSample())
        return ApiResponse.success()
    }
}
