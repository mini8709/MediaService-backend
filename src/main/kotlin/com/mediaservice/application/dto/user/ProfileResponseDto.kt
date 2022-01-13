package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile
import com.mediaservice.domain.User

data class ProfileResponseDto(
    val user: User,
    val name: String,
    val rate: String,
    val main_image: String
) {
    companion object {
        fun from(profile: Profile) = ProfileResponseDto(
            user = profile.user,
            name = profile.name,
            rate = profile.rate,
            main_image = profile.mainImage
        )
    }
}
