package com.mediaservice.domain.repository

import com.mediaservice.domain.Like
import com.mediaservice.domain.LikeTable
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository

@Repository
class LikeRepository {
    fun save(like: Like): Like {
        LikeTable.insert {
            it[profile] = like.profile.id
            it[mediaAllSeries] = like.mediaAllSeries.id
        }

        return like
    }
}
