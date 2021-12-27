package com.mediaservice.config

import com.mediaservice.domain.Role
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
            .setExpiration(Date(now.time + validTime))
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val id: String = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).body.subject
        return UsernamePasswordAuthenticationToken(id, null, ArrayList())
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.replace("Bearer ", "")
    }

    fun validateToken(jwtToken: String?, request: HttpServletRequest): Boolean {
        val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(jwtToken)
        return !claims.body.expiration.before(Date())
    }
}
