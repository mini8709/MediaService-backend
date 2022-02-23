package com.mediaservice.application.dto.user

data class SignUpVerifyAuthRequestDto(
    val signUpKey: String,
    val email: String
)
