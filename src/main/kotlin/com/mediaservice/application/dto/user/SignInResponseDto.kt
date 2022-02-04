package com.mediaservice.application.dto.user

data class SignInResponseDto(
    val accessToken: String,
    val profileList: List<ProfileResponseDto>
) {
    companion object {
        fun from(
            accessToken: String,
            profileList: List<ProfileResponseDto>
        ) = SignInResponseDto(
            accessToken = accessToken,
            profileList = profileList
        )
    }
}
