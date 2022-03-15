package com.mediaservice.domain.repository

import com.mediaservice.domain.MediaContents
import com.mediaservice.domain.MediaContentsEntity
import com.mediaservice.domain.MediaContentsTable
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MediaContentsRepository {
    fun findById(id: UUID): MediaContents? {
        return MediaContentsEntity.findById(id)?.let {
            return MediaContents.from(it)
        }
    }

    fun save(mediaContents: MediaContents): MediaContents {
        mediaContents.id =
            (
                MediaContentsTable.insert {
                    it[title] = mediaContents.title
                    it[synopsis] = mediaContents.synopsis
                    it[trailer] = mediaContents.trailer
                    it[thumbnail] = mediaContents.thumbnail
                    it[rate] = mediaContents.rate
                    it[isSeries] = mediaContents.isSeries
                    it[isDeleted] = false
                } get MediaContentsTable.id
                ).value
        return mediaContents
    }

    fun update(mediaContents: MediaContents): MediaContents? {
        return MediaContentsEntity.findById(mediaContents.id!!)?.let {
            it.title = mediaContents.title
            it.synopsis = mediaContents.synopsis
            it.trailer = mediaContents.trailer
            it.thumbnail = mediaContents.thumbnail
            it.rate = mediaContents.rate
            it.isSeries = mediaContents.isSeries

            return MediaContents.from(it)
        }
    }

    fun deleteById(id: UUID): MediaContents? {
        return MediaContentsEntity.findById(id)?.let {
            it.isDeleted = true
            return MediaContents.from(it)
        }
    }
}
