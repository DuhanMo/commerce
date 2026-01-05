package org.duhan.commerce.gateway.filter

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

class AuthorizationHeaderFilterTest {

    private lateinit var filter: AuthorizationHeaderFilter
    private val env: Environment = mockk()
    private val chain: GatewayFilterChain = mockk()
    private val secretKey = "v9y\$B&E)H@McQfTjWnZr4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?E(G+KbPeShVmYq"

    @BeforeEach
    fun setUp() {
        filter = AuthorizationHeaderFilter(env)
        every { env.getProperty("token.secret") } returns secretKey
        every { chain.filter(any()) } returns Mono.empty()
    }

    // 테스트용 토큰 생성
    private fun createToken(userId: String, role: String): String {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
        return Jwts.builder()
            .subject(userId)
            .claim("role", role)
            .signWith(key)
            .compact()
    }

    @Test
    @DisplayName("성공: 유효한 ADMIN 토큰으로 ADMIN 권한이 필요한 경로에 접근")
    fun successWithAdminRole() {
        // given
        val token = createToken("admin-user", "ADMIN")
        val request = MockServerHttpRequest.get("/api/admin/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .build()
        val exchange = MockServerWebExchange.from(request)
        val config = AuthorizationHeaderFilter.Config(role = "ADMIN")

        // chain.filter()에 전달되는 exchange를 캡처하기 위한 슬롯
        val exchangeSlot = slot<ServerWebExchange>()
        every { chain.filter(capture(exchangeSlot)) } returns Mono.empty()

        // when
        filter.apply(config).filter(exchange, chain).block()

        // then
        // 원본 exchange가 아닌, 필터를 거쳐 새로 생성된(mutated) exchange에서 request를 추출
        val modifiedRequest = exchangeSlot.captured.request

        assert(modifiedRequest.headers.getFirst("X-User-Id") == "admin-user")
        assert(modifiedRequest.headers.getFirst("X-User-Role") == "ADMIN")
        verify { chain.filter(any()) }
    }

    @Test
    @DisplayName("실패: USER 권한 토큰으로 ADMIN 경로 접근 시 403 에러")
    fun failWithForbidden() {
        // given
        val token = createToken("normal-user", "USER")
        val request = MockServerHttpRequest.get("/api/admin/test")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .build()
        val exchange = MockServerWebExchange.from(request)
        val config = AuthorizationHeaderFilter.Config(role = "ADMIN")

        // when
        filter.apply(config).filter(exchange, chain).block()

        // then
        assert(exchange.response.statusCode == HttpStatus.FORBIDDEN)
    }
}