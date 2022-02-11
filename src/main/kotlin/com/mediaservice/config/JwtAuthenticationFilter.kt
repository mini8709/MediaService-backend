package com.mediaservice.config

import com.mediaservice.domain.repository.RefreshTokenRepository
import io.jsonwebtoken.io.IOException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val accessToken: String? = this.tokenProvider.resolveAccessToken(request as HttpServletRequest)
        val refreshToken: String? = this.tokenProvider.resolveRefreshToken(request)

        if (accessToken != null && this.tokenProvider.validateToken(
                accessToken,
                refreshToken,
                request,
                response as HttpServletResponse,
                refreshTokenRepository
            )
        ) {
            val auth: Authentication = this.tokenProvider.getAuthentication(accessToken, request)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }
}
