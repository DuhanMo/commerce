package org.duhan.commerce.core.api.controller.v1.request

import org.duhan.commerce.core.domain.Sample
import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType

data class CreateSampleRequest(val name: String) {
    fun toSample(): Sample {
        if (name.isBlank()) throw CoreException(ErrorType.INVALID_REQUEST, "이름은 빈값일 수 없습니다")
        return Sample(name)
    }
}
