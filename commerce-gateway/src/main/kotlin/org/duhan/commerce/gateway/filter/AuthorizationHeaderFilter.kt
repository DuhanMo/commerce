package org.duhan.commerce.gateway.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import javax.crypto.SecretKey

@Component
class AuthorizationHeaderFilter(
    private val env: Environment,
) : AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>(Config::class.java) {

    private val log = LoggerFactory.getLogger(javaClass)

    // 설정을 통해 특정 라우트마다 필요한 권한을 다르게 부여할 수 있습니다.
    class Config(
        var role: String? = null,
    )

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val request = exchange.request

            // 1. Authorization 헤더 존재 확인
            if (!request.headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return@GatewayFilter onError(exchange, "인증 헤더가 누락되었습니다.", HttpStatus.UNAUTHORIZED)
            }

            val authHeader = request.headers[HttpHeaders.AUTHORIZATION]?.get(0)

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // '필수 권한(Admin/Partner)'이 필요한 페이지라면 에러 처리
                return@GatewayFilter if (!config.role.isNullOrEmpty()) {
                    onError(exchange, "인증이 필요한 서비스입니다.", HttpStatus.UNAUTHORIZED)
                } else {
                    // 권한이 필요 없는 일반 페이지라면 게스트로 통과
                    chain.filter(exchange)
                }
            }

            // 2. JWT 토큰 추출
            val jwt = authHeader.replace("Bearer ", "")

            // 3. 토큰 검증 및 클레임 추출
            val claims = getAllClaimsFromJwt(jwt)
                ?: return@GatewayFilter onError(exchange, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED)

            val userId = claims.subject
            val userRole = claims["role"] as? String ?: return@GatewayFilter onError(
                exchange,
                "토큰 내 권한 정보가 누락되었습니다.",
                HttpStatus.UNAUTHORIZED,
            )

            // 4. 설정된 권한(Config.role)이 있다면 유저의 권한과 비교
            if (!config.role.isNullOrEmpty() && config.role != userRole) {
                return@GatewayFilter onError(exchange, "해당 리소스에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN)
            }

            // 5. 하위 서비스로 식별자 및 역할 전달 (헤더 조작)
            val modifiedRequest = request.mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", userRole)
                .build()

            log.info("인증 성공: 사용자ID={}, 역할={}", userId, userRole)

            chain.filter(exchange.mutate().request(modifiedRequest).build())
        }
    }

    private fun getAllClaimsFromJwt(jwt: String): Claims? {
        return try {
            val secretKeyString = env.getProperty("jwt.secret-key")
                ?: throw IllegalStateException("토큰 비밀키가 설정되지 않았습니다.")

            val key: SecretKey = Keys.hmacShaKeyFor(secretKeyString.toByteArray(StandardCharsets.UTF_8))

            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .payload
        } catch (e: Exception) {
            log.error("JWT 검증 오류: {}", e.message)
            null
        }
    }

    private fun onError(exchange: ServerWebExchange, err: String, httpStatus: HttpStatus): Mono<Void> {
        val response = exchange.response
        response.statusCode = httpStatus
        response.headers.contentType = MediaType.APPLICATION_JSON

        val errorBody = """
            {
                "timestamp": "${LocalDateTime.now()}",
                "status": ${httpStatus.value()},
                "error": "${httpStatus.reasonPhrase}",
                "message": "$err",
                "path": "${exchange.request.path}"
            }
        """.trimIndent()

        val buffer = response.bufferFactory().wrap(errorBody.toByteArray(StandardCharsets.UTF_8))
        return response.writeWith(Mono.just(buffer))
    }
}
