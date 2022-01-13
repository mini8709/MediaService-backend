package com.mediaservice.web

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpRequestDto: SignUpRequestDto): UserResponseDto {
        return this.userService.signUp(signUpRequestDto)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody signInRequestDto: SignInRequestDto): String {
        return this.userService.signIn(signInRequestDto)
    }
}
