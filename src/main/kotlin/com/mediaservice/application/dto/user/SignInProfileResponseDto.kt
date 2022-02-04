package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile

data class SignInProfileResponseDto(
    val name: String,
    val rate: String,
    val mainImage: String
) {
    companion object {
        fun from(profile: Profile) = SignInProfileResponseDto(
            name = profile.name,
            rate = profile.rate,
            mainImage = profile.mainImage
        )
    }
}
