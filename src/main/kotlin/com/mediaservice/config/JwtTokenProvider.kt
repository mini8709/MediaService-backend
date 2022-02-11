package com.mediaservice.config

import com.mediaservice.domain.RefreshToken
import com.mediaservice.domain.Role
import com.mediaservice.domain.repository.RefreshTokenRepository
import com.mediaservice.exception.ErrorCode
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenProvider(env: Environment, refreshTokenRepository: RefreshTokenRepository) {
    val accessKey = env.getProperty("JWT.access_secret")?.toByteArray()
    val refreshKey = env.getProperty("JWT.refresh_secret")?.toByteArray()
    val accessValidTime = 10
    val refreshValidTime = 1000L * 60 * 60 * 24 * 7

    fun createAccessToken(ID: UUID, role: Role): String {
        val now = Date()
        val claims = Jwts.claims().setSubject(ID.toString())
        claims.put("role", role)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + this.accessValidTime))
            .signWith(Keys.hmacShaKeyFor(this.accessKey), SignatureAlgorithm.HS512)
            .compact()
    }

    fun createRefreshToken(): RefreshToken {
        val now = Date()

        return RefreshToken(
            Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(Date(now.time + this.refreshValidTime))
                .signWith(Keys.hmacShaKeyFor(this.refreshKey), SignatureAlgorithm.HS512)
                .compact()
        )
    }

    fun getAuthentication(token: String?, request: HttpServletRequest): Authentication {
        val id = try {
            Jwts.parserBuilder().setSigningKey(this.accessKey).build().parseClaimsJws(token).body.subject
        } catch (e: ExpiredJwtException) {
            e.claims.subject
        }
        return UsernamePasswordAuthenticationToken(id, null, ArrayList())
    }

    fun resolveAccessToken(request: HttpServletRequest): String? {
        return request.getHeader("access_token")?.replace("Bearer ", "")
    }

    fun resolveRefreshToken(request: HttpServletRequest): String? {
        return request.getHeader("refresh_token")?.replace("Bearer ", "")
    }

    fun validateToken(
        accessToken: String?,
        refreshToken: String?,
        request: HttpServletRequest,
        response: HttpServletResponse,
        refreshTokenRepository: RefreshTokenRepository
    ): Boolean {
        val accessClaims = try {
            Jwts.parserBuilder().setSigningKey(this.accessKey).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        } catch (e: Exception) {
            request.setAttribute("errorCode", ErrorCode.INVALID_JWT)
            return false
        }

        if (accessClaims.expiration.before(Date())) {
            try {
                Jwts.parserBuilder()
                    .setSigningKey(this.refreshKey)
                    .build()
                    .parseClaimsJws(refreshToken)
            } catch (e: Exception) {
                request.setAttribute("errorCode", ErrorCode.INVALID_JWT)
                return false
            }

            if (refreshTokenRepository.findById(refreshToken!!).isPresent) {
                response.setHeader(
                    "access_token",
                    this.createAccessToken(
                        UUID.fromString(
                            accessClaims.subject
                        ),
                        Role.valueOf(accessClaims["role"] as String)
                    )
                )
            } else {
                request.setAttribute("errorCode", ErrorCode.INVALID_JWT)
                return false
            }
        }

        return true
    }

    fun checkAdmin(jwtToken: String?, request: HttpServletRequest): Boolean {
        val claims = try {
            Jwts.parserBuilder().setSigningKey(this.accessKey).build().parseClaimsJws(jwtToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }

        if (claims["role"]?.equals(Role.ADMIN.toString()) == false) {
            request.setAttribute("errorCode", ErrorCode.NOT_ACCESSIBLE)
            return false
        }

        return true
    }
}
