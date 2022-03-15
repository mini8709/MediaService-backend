package com.mediaservice.domain.repository

import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.MediaSeriesEntity
import com.mediaservice.domain.MediaSeriesTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaSeriesRepository {
    fun findById(id: UUID): MediaSeries? {
        return MediaSeriesEntity.findById(id)?.let {
            return MediaSeries.from(it)
        }
    }

    fun findByMediaContentsId(id: UUID): List<MediaSeries> {
        return MediaSeriesEntity.find {
            MediaSeriesTable.mediaContents eq id and (MediaSeriesTable.isDeleted eq false)
        }.sortedBy {
            it.order
        }.map {
            MediaSeries.from(it)
        }.toList()
    }

    fun save(mediaSeries: MediaSeries): MediaSeries {
        mediaSeries.id = (
            MediaSeriesTable.insert {
                it[title] = mediaSeries.title
                it[order] = mediaSeries.order
                it[isDeleted] = false
                it[mediaContents] = mediaSeries.mediaContents.id
            } get MediaSeriesTable.id
            ).value
        return mediaSeries
    }

    fun update(mediaSeries: MediaSeries): MediaSeries? {
        return MediaSeriesEntity.findById(mediaSeries.id!!)?.let {
            it.title = mediaSeries.title
            it.order = mediaSeries.order

            return MediaSeries.from(it)
        }
    }

    fun deleteById(id: UUID): MediaSeries? {
        return MediaSeriesEntity.findById(id)?.let {
            it.isDeleted = true
            return MediaSeries.from(it)
        }
    }
}
