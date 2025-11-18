package org.duhan.commerce.core.api.controller.v1.response

import org.duhan.commerce.core.domain.Sample

data class SampleResponse(val name: String) {
    companion object {
        fun of(sample: Sample): SampleResponse {
            return SampleResponse(sample.name)
        }

        fun of(samples: List<Sample>): List<SampleResponse> {
            return samples.map { of(it) }
        }
    }
}
