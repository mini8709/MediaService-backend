package com.mediaservice.application.dto

import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries

data class MediaSeriesResponseDto(
    val title: String,
    val order: Int,
    val mediaAllSeries: MediaAllSeries
) {
    companion object {
        fun from(mediaSeries: MediaSeries) = MediaSeriesResponseDto(
            title = mediaSeries.title,
            order = mediaSeries.order,
            mediaAllSeries = mediaSeries.mediaAllSeries
        )
    }
}
