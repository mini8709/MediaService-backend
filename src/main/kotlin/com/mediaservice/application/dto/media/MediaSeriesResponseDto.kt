package com.mediaservice.application.dto.media

import com.mediaservice.domain.MediaSeries

data class MediaSeriesResponseDto(
    val title: String,
    val order: Int
) {
    companion object {
        fun from(mediaSeries: MediaSeries) = MediaSeriesResponseDto(
            title = mediaSeries.title,
            order = mediaSeries.order
        )
    }
}
