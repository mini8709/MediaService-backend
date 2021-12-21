package com.mediaservice.web

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.UserRequestDto
import com.mediaservice.application.dto.UserResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody userRequestDto: UserRequestDto): UserResponseDto {
        return this.userService.signUp(userRequestDto)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody userRequestDto: UserRequestDto): String {
        return this.userService.signIn(userRequestDto)
    }
}
