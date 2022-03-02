package com.mediaservice.web

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.user.PasswordFindRequestDto
import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignInResponseDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.SignUpVerifyAuthRequestDto
import com.mediaservice.application.dto.user.SignUpVerifyMailRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {
    @PostMapping("/verify-mail")
    fun signUpVerifyMail(@RequestBody signUpVerifyMailRequestDto: SignUpVerifyMailRequestDto): String {
        return this.userService.signUpVerifyMail(signUpVerifyMailRequestDto)
    }

    @PostMapping("/verify-auth")
    fun signUpVerifyAuth(@RequestBody signUpVerifyAuthRequestDto: SignUpVerifyAuthRequestDto) {
        return this.userService.signUpVerifyAuth(signUpVerifyAuthRequestDto)
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpRequestDto: SignUpRequestDto): UserResponseDto {
        return this.userService.signUp(signUpRequestDto)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody signInRequestDto: SignInRequestDto): SignInResponseDto {
        return this.userService.signIn(signInRequestDto)
    }

    @PostMapping("/password")
    fun findPassword(@RequestBody passwordFindRequestDto: PasswordFindRequestDto): UserResponseDto {
        return this.userService.findPassword(passwordFindRequestDto)
    }
}
