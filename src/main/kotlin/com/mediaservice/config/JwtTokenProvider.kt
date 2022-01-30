package com.mediaservice.config

import com.mediaservice.domain.Role
import com.mediaservice.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
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

@Component
class JwtTokenProvider(env: Environment) {
    val signingKey = env.getProperty("JWT.secret")?.toByteArray()
    val validTime = 1000L * 60 * 60 * 24 * 7

    fun createToken(ID: UUID, role: Role): String {
        val now = Date()
        val claims = Jwts.claims().setSubject(ID.toString())
        claims.put("role", role)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + this.validTime))
            .signWith(Keys.hmacShaKeyFor(this.signingKey), SignatureAlgorithm.HS512)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val id: String = Jwts.parserBuilder().setSigningKey(this.signingKey).build().parseClaimsJws(token).body.subject
        return UsernamePasswordAuthenticationToken(id, null, ArrayList())
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.replace("Bearer ", "")
    }

    fun validateToken(jwtToken: String?, request: HttpServletRequest): Boolean {
        val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(this.signingKey).build().parseClaimsJws(jwtToken)

        if (claims.body.expiration.before(Date())) {
            request.setAttribute("errorCode", ErrorCode.INVALID_JWT)
            return false
        }

        return true
    }

    fun checkAdmin(jwtToken: String?, request: HttpServletRequest): Boolean {
        val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(this.signingKey).build().parseClaimsJws(jwtToken)

        if (claims.body["role"]?.equals(Role.ADMIN.toString()) == false) {
            request.setAttribute("errorCode", ErrorCode.NOT_ACCESSIBLE)
            return false
        }

        return true
    }
}
