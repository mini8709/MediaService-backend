package com.mediaservice.application.dto.user

data class PasswordUpdateRequestDto(
    val srcPassword: String,
    val dstPassword: String
)
