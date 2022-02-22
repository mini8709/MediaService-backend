package com.mediaservice.domain.repository

import com.mediaservice.domain.Like
import com.mediaservice.domain.LikeTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    fun delete(like: Like): Like {
        LikeTable.deleteWhere {
            LikeTable.profile eq like.profile.id and (LikeTable.mediaAllSeries eq like.mediaAllSeries.id)
        }

        return like
    }

    fun isExist(like: Like): Boolean {
        val count = LikeTable.select {
            LikeTable.profile eq like.profile.id and (LikeTable.mediaAllSeries eq like.mediaAllSeries.id)
        }.count()

        return count != 0L
    }
}
