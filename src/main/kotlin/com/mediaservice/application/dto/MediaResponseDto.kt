package com.mediaservice.application.dto

import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaSeries

data class MediaResponseDto(
    val name: String,
    val synopsis: String,
    val order: Int,
    val mediaSeries: MediaSeries
) {
    companion object {
        fun from(media: Media) = MediaResponseDto(
            name = media.name,
            synopsis = media.synopsis,
            order = media.order,
            mediaSeries = media.mediaSeries
        )
    }
}
