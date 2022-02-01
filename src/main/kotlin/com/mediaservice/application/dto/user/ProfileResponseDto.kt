package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile
import com.mediaservice.domain.User

data class ProfileResponseDto(
    val user: User,
    val name: String,
    val rate: String,
    val mainImage: String,
    val isDeleted: Boolean
) {
    companion object {
        fun from(profile: Profile) = ProfileResponseDto(
            user = profile.user,
            name = profile.name,
            rate = profile.rate,
            mainImage = profile.mainImage,
            isDeleted = profile.isDeleted
        )
    }
}
