package com.mediaservice.domain.repository

import com.mediaservice.domain.MediaContents
import com.mediaservice.domain.MediaContentsEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaContentsRepository {
    fun findById(id: UUID): MediaContents? {
        return MediaContentsEntity.findById(id)?.let {
            return MediaContents.from(it)
        }
    }
}
