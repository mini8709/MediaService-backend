package com.mediaservice.domain.repository

import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaAllSeriesEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaAllSeriesRepository {
    fun findById(id: UUID): MediaAllSeries? {
        return MediaAllSeriesEntity.findById(id)?.let {
            return MediaAllSeries.from(it)
        }
    }
}
