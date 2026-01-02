package org.duhan.commerce.support.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        // 권한이 없는 사용자가 접근했을 때 403 에러를 응답
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: 권한이 없습니다.")
    }
}
