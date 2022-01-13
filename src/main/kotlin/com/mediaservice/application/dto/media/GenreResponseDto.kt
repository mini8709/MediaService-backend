package com.mediaservice.application.dto.media

import com.mediaservice.domain.Genre
import java.util.UUID

data class GenreResponseDto(
    val id: UUID,
    val name: String
) {
    companion object {
        fun from(genre: Genre) = GenreResponseDto(
            id = genre.id!!,
            name = genre.name
        )
    }
}
