package com.mediaservice.application.dto.user

import com.mediaservice.domain.RefreshToken

data class SignInResponseDto(
    val accessToken: String,
    val refreshToken: RefreshToken,
    val profileList: List<ProfileResponseDto>
) {
    companion object {
        fun from(
            accessToken: String,
            refreshToken: RefreshToken,
            profileList: List<ProfileResponseDto>
        ) = SignInResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            profileList = profileList
        )
    }
}
