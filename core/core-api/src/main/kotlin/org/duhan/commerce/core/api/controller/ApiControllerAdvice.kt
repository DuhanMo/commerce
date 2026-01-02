package org.duhan.commerce.core.api.controller

import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.core.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * @Valid 또는 @Validated 검증 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Any>> {
        log.info("ValidationException : {}", e.message)

        val errorMap = e.bindingResult.fieldErrors.associate { fieldError ->
            fieldError.field to (fieldError.defaultMessage ?: "입력값이 올바르지 않습니다.")
        }

        val errorType = ErrorType.INVALID_REQUEST
        return ResponseEntity(
            ApiResponse.error(errorType, errorMap),
            errorType.status,
        )
    }

    /**
     * JSON 파싱 실패 또는 타입 불일치 (예: 숫자에 문자열 넣음, 콤마 누락 등)
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Any>> {
        log.warn("HttpMessageNotReadableException : {}", e.message)

        val errorType = ErrorType.INVALID_REQUEST
        val customMessage = "요청 본문(JSON)을 읽을 수 없습니다. 형식을 확인해주세요."

        return ResponseEntity(
            ApiResponse.error(errorType, customMessage),
            errorType.status,
        )
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(e: AuthorizationDeniedException): ResponseEntity<ApiResponse<Any>> {
        log.warn("AuthorizationDeniedException : {}", e.message)

        return ResponseEntity(
            ApiResponse.error(ErrorType.ACCESS_DENIED),
            ErrorType.ACCESS_DENIED.status,
        )
    }

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> {
        when (e.errorType.logLevel) {
            LogLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            LogLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }
        return ResponseEntity(ApiResponse.error(e.errorType, e.data), e.errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(ErrorType.DEFAULT_ERROR), ErrorType.DEFAULT_ERROR.status)
    }
}
