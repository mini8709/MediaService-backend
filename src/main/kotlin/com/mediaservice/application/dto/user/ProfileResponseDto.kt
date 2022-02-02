package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile
import java.util.UUID

data class ProfileResponseDto(
    val profileId: UUID,
    val name: String,
    val mainImage: String,
    val rate: String
) {
    companion object {
        fun from(profile: Profile) = ProfileResponseDto(
            profileId = profile.id!!,
            mainImage = profile.mainImage,
            name = profile.name,
            rate = profile.rate
        )
    }
}
