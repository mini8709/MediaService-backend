package com.mediaservice.domain.repository

import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.MediaSeriesEntity
import com.mediaservice.domain.MediaSeriesTable
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaSeriesRepository {
    fun findById(id: UUID): MediaSeries? {
        return MediaSeriesEntity.findById(id)?.let {
            return MediaSeries.from(it)
        }
    }

    fun findByMediaAllSeriesId(id: UUID): List<MediaSeries>? {
        return MediaSeriesEntity.find {
            MediaSeriesTable.mediaAllSeries eq id and (MediaSeriesTable.isDeleted eq false)
        }.sortedBy {
            it.order
        }.map {
            MediaSeries.from(it)
        }.toList()
    }
}
