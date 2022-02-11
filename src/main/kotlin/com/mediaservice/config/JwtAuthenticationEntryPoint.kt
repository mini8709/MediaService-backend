package com.mediaservice.config

import com.mediaservice.exception.ErrorCode
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val errorCode = request?.getAttribute("errorCode")

        if (errorCode == ErrorCode.INVALID_JWT) {
            this.setResponse(response!!, ErrorCode.INVALID_JWT, "Sign in again")
            return
        }

        if (errorCode == ErrorCode.NOT_ACCESSIBLE) {
            this.setResponse(response!!, ErrorCode.NOT_ACCESSIBLE, "Do not have permission to access")
            return
        }
    }

    private fun setResponse(
        response: HttpServletResponse,
        errorCode: ErrorCode,
        message: String
    ) {
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.println(
            "{" +
                "\"errorCode\" : \"" + errorCode.code + "\"," +
                "\"message\" : \"" + message + "\"," +
                "\"timeStamp\" : \"" + LocalDateTime.now() + "\"" +
                "}"
        )
    }
}
