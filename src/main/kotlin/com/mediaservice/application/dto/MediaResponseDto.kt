package com.mediaservice.application.dto

import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaSeries

data class MediaResponseDto(
    val name: String,
    val synopsis: String,
    val order: Int,
    val url: String,
    val thumbnail: String,
    val runningTime: Int,
    val mediaSeries: MediaSeries
) {
    companion object {
        fun from(media: Media) = MediaResponseDto(
            name = media.name,
            synopsis = media.synopsis,
            order = media.order,
            url = media.url,
            thumbnail = media.thumbnail,
            runningTime = media.runningTime,
            mediaSeries = media.mediaSeries
        )
    }
}
