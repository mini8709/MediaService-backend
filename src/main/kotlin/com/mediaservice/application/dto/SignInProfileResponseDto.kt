package com.mediaservice.application.dto

import com.mediaservice.domain.Profile

data class SignInProfileResponseDto(
    val name: String,
    val rate: String,
    val main_image: String
) {
    companion object {
        fun from(profile: Profile) = SignInProfileResponseDto(
            name = profile.name,
            rate = profile.rate,
            main_image = profile.mainImage
        )
    }
}
