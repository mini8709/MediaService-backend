package com.mediaservice.application.dto.user

import java.util.UUID

data class LikeRequestDto(
    val profileId: UUID,
    val mediaAllSeriesId: UUID
)
