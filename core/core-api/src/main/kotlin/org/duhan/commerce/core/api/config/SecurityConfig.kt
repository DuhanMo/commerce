package org.duhan.commerce.core.api.config

import org.duhan.commerce.support.jwt.JwtAccessDeniedHandler
import org.duhan.commerce.support.jwt.JwtAuthenticationEntryPoint
import org.duhan.commerce.support.jwt.JwtAuthenticationFilter
import org.duhan.commerce.support.jwt.TokenProvider
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeHttpRequests {
                // 공통/가입/로그인/헬스체크 API는 전체 허용
                authorize("/v1/auth/**", permitAll)
                authorize("/health", permitAll)

                // 나머지 모든 요청은 인증 필요
                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(
                JwtAuthenticationFilter(tokenProvider),
            )
            exceptionHandling {
                authenticationEntryPoint = jwtAuthenticationEntryPoint // 401 처리
                accessDeniedHandler = jwtAccessDeniedHandler // 403 처리
            }
            headers {
                // H2 Console 사용 시 필요
                frameOptions { sameOrigin = true }
            }
        }
        return http.build()
    }
}
