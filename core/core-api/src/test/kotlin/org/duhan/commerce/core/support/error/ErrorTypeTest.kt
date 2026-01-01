package org.duhan.commerce.core.support.error

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ErrorTypeTest {
    @Test
    fun `ErrorCode 중복 사용 확인`() {
        val codes = ErrorType.entries.map { it.code }
        val duplicates = codes.groupingBy { it }.eachCount().filter { it.value > 1 }.keys

        assertTrue(duplicates.isEmpty(), "중복된 ErrorCode가 있습니다: $duplicates")
    }

    @Test
    fun `ErrorCode가 ErrorType에서 모두 사용되는지_확인`() {
        val declaredCodes = ErrorCode.entries.toSet()
        val usedCodes = ErrorType.entries.map { it.code }.toSet()

        val unused = declaredCodes - usedCodes

        assertTrue(unused.isEmpty(), "사용되지 않은 ErrorCode가 있습니다: $unused")
    }
}
