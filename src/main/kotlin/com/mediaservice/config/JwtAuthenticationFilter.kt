package com.mediaservice.config

import io.jsonwebtoken.io.IOException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(private val tokenProvider: JwtTokenProvider) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token: String? = this.tokenProvider.resolveToken(request as HttpServletRequest)
        if (token != null && this.tokenProvider.validateToken(token, request)) {
            val auth: Authentication = this.tokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }
}
