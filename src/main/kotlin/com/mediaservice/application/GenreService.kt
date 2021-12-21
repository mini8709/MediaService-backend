package com.mediaservice.application

import com.mediaservice.application.dto.GenreCreateRequestDto
import com.mediaservice.application.dto.GenreResponseDto
import com.mediaservice.domain.Genre
import com.mediaservice.domain.repository.GenreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GenreService(
    private val genreRepository: GenreRepository
) {
    @Transactional
    fun create(
        genreCreateRequestDto: GenreCreateRequestDto
    ): GenreResponseDto {
        return GenreResponseDto.from(
            this.genreRepository.save(
                Genre.of(
                    genreCreateRequestDto.name
                )
            )
        )
    }
}
