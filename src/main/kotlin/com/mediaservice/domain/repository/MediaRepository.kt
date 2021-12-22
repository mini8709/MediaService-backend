package com.mediaservice.domain.repository

import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaRepository {
    fun findById(id: UUID): Media? {
        return MediaEntity.findById(id)?.let {
            return Media.from(it)
        }
    }
}
