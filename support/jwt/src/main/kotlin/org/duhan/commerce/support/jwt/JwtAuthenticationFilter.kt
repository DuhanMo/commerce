package org.duhan.commerce.support.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // 1. 헤더에서 토큰 추출
        val token = resolveToken(request)

        // 2. 토큰이 유효한지 확인
        if (token != null && tokenProvider.validateToken(token)) {
            // 3. 토큰에서 필요한 모든 정보 추출
            val accountId = tokenProvider.getAccountId(token)
            val email = tokenProvider.getSubject(token)
            val role = tokenProvider.getRole(token)

            // 4. 인증 객체 생성
            // Principal에 UserContext 객체를 넣어 서비스 레이어에서 ID와 Email 모두 접근 가능하게 함
            val userContext = UserContext(id = accountId, email = email, role = role)
            val authorities = listOf(SimpleGrantedAuthority(role))

            val authentication = UsernamePasswordAuthenticationToken(
                userContext, // Principal
                null, // Credentials
                authorities,
            )

            // 5. 시큐리티 컨텍스트에 저장
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
