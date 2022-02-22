package com.mediaservice.application.dto.media

import java.util.UUID

data class MediaDetailRequestDto(
    val mediaAllSeriesId: UUID,
    val profileId: UUID
)
