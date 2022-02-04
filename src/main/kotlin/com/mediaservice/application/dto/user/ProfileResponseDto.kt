package com.mediaservice.application.dto.user

import com.mediaservice.domain.Profile
import java.util.UUID

data class ProfileResponseDto(
    val id: UUID,
    val name: String,
    val mainImage: String,
    val rate: String
) {
    companion object {
        fun from(profile: Profile) = ProfileResponseDto(
            id = profile.id!!,
            mainImage = profile.mainImage,
            name = profile.name,
            rate = profile.rate,
        )
    }
}
