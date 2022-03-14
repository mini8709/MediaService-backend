package com.mediaservice.config

import com.mediaservice.exception.ErrorCode
import io.jsonwebtoken.io.IOException
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RoleAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = this.tokenProvider.resolveAccessToken(request)

        if (!request.method.equals("GET", true) &&
            (token == null || !this.tokenProvider.checkAdmin(token, request))
        ) {
            request.setAttribute("errorCode", ErrorCode.NOT_ACCESSIBLE)
            jwtAuthenticationEntryPoint.commence(request, response, null)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}
