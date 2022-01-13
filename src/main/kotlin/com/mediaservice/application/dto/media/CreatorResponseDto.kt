package com.mediaservice.application.dto.media

import com.mediaservice.domain.Creator
import java.util.UUID

data class CreatorResponseDto(
    val id: UUID,
    val name: String
) {
    companion object {
        fun from(creator: Creator) = CreatorResponseDto(
            id = creator.id!!,
            name = creator.name
        )
    }
}
