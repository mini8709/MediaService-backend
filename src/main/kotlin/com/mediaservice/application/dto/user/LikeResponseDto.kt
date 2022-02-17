package com.mediaservice.application.dto.user

import com.mediaservice.domain.Like
import java.util.UUID

data class LikeResponseDto(
    val profileId: UUID,
    val mediaAllSeriesId: UUID
) {
    companion object {
        fun from(like: Like) = LikeResponseDto(
            profileId = like.profile.id!!,
            mediaAllSeriesId = like.mediaAllSeries.id
        )
    }
}
