package com.mediaservice.domain.repository

import com.mediaservice.domain.Creator
import com.mediaservice.domain.CreatorEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CreatorRepository {
    fun save(creator: Creator): Creator {
        return CreatorEntity.new {
            name = creator.name
            isDeleted = creator.isDeleted
        }.let { Creator.from(it) }
    }

    fun findById(id: UUID): Creator? {
        return CreatorEntity.findById(id)?.let {
            Creator.from(it)
        }
    }

    fun update(id: UUID, creator: Creator): Creator? {
        return CreatorEntity.findById(id)?.let {
            it.name = creator.name
            return Creator.from(it)
        }
    }
}
