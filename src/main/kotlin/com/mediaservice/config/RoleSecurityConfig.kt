package com.mediaservice.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.Filter

@Configuration
class RoleSecurityConfig(
    private val tokenProvider: JwtTokenProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) : WebMvcConfigurer {
    private val urlPatterns = arrayListOf(
        "/api/v1/actors/*",
        "/api/v1/creators/*",
        "/api/v1/genres/*",
        "/api/v1/media/*",
        "/api/v1/media-contents/*"
    )

    @Bean
    fun getRuleFilter(): FilterRegistrationBean<Filter> {
        val registrationBean = FilterRegistrationBean<Filter>(
            RoleAuthenticationFilter(tokenProvider, jwtAuthenticationEntryPoint)
        )

        urlPatterns.stream().forEach { url -> registrationBean.addUrlPatterns(url) }
        registrationBean.order = 0

        return registrationBean
    }
}
