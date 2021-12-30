package com.mediaservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val tokenProvider: JwtTokenProvider) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/api/v1/auth/**"
            )
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(
                JwtAuthenticationFilter(this.tokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/v2/api-docs", "/swagger-resources/**",
            "/webjars/**", "/swagger/**", "/swagger-ui/**"
        )
    }
}
