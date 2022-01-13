package com.mediaservice.application.dto.media

import com.mediaservice.domain.MediaAllSeries

data class MediaAllSeriesResponseDto(
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean
) {
    companion object {
        fun from(mediaAllSeries: MediaAllSeries) = MediaAllSeriesResponseDto(
            title = mediaAllSeries.title,
            synopsis = mediaAllSeries.synopsis,
            trailer = mediaAllSeries.trailer,
            thumbnail = mediaAllSeries.thumbnail,
            rate = mediaAllSeries.rate,
            isSeries = mediaAllSeries.isSeries
        )
    }
}
