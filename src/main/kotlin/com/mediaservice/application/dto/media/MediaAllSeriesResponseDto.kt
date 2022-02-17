package com.mediaservice.application.dto.media

import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaAllSeriesTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

data class MediaAllSeriesResponseDto(
    val id: UUID,
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean
) {
    companion object {
        fun from(mediaAllSeries: MediaAllSeries) = MediaAllSeriesResponseDto(
            id = mediaAllSeries.id,
            title = mediaAllSeries.title,
            synopsis = mediaAllSeries.synopsis,
            trailer = mediaAllSeries.trailer,
            thumbnail = mediaAllSeries.thumbnail,
            rate = mediaAllSeries.rate,
            isSeries = mediaAllSeries.isSeries
        )

        fun from(resultRow: ResultRow) = MediaAllSeriesResponseDto(
            id = resultRow[MediaAllSeriesTable.id].value,
            title = resultRow[MediaAllSeriesTable.title],
            synopsis = resultRow[MediaAllSeriesTable.synopsis],
            trailer = resultRow[MediaAllSeriesTable.trailer],
            thumbnail = resultRow[MediaAllSeriesTable.thumbnail],
            rate = resultRow[MediaAllSeriesTable.rate],
            isSeries = resultRow[MediaAllSeriesTable.isSeries]
        )
    }
}
