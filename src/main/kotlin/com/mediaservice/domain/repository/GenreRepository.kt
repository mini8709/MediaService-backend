package com.mediaservice.domain.repository

import com.mediaservice.domain.Genre
import com.mediaservice.domain.GenreEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class GenreRepository {
    fun save(genre: Genre): Genre {
        return GenreEntity.new {
            name = genre.name
        }.let { return Genre.from(it) }
    }

    fun findById(id: UUID): Genre? {
        return GenreEntity.findById(id)?.let {
            Genre.from(it)
        }
    }
}
