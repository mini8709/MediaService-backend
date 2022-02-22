package com.mediaservice.domain.repository

import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaEntity
import com.mediaservice.domain.MediaTable
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaRepository {
    fun findById(id: UUID): Media? {
        return MediaEntity.findById(id)?.let {
            return Media.from(it)
        }
    }

    fun findByMediaSeriesId(id: UUID): List<Media>? {
        return MediaEntity.find {
            MediaTable.mediaSeries eq id and (MediaTable.isDeleted eq false)
        }.sortedBy {
            it.order
        }.map {
            Media.from(it)
        }.toList()
    }
}
