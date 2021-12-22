package com.mediaservice.domain.repository

import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.MediaSeriesEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaSeriesRepository {
    fun findById(id: UUID): MediaSeries? {
        return MediaSeriesEntity.findById(id)?.let {
            return MediaSeries.from(it)
        }
    }
}
