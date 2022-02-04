package com.mediaservice.application

import com.mediaservice.application.dto.media.GenreCreateRequestDto
import com.mediaservice.application.dto.media.GenreResponseDto
import com.mediaservice.application.dto.media.GenreUpdateRequestDto
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.application.validator.Validator
import com.mediaservice.domain.Genre
import com.mediaservice.domain.repository.GenreRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GenreService(
    private val genreRepository: GenreRepository
) {
    @Transactional
    fun create(genreCreateRequestDto: GenreCreateRequestDto): GenreResponseDto {
        return GenreResponseDto.from(
            this.genreRepository.save(
                Genre.of(genreCreateRequestDto.name)
            )
        )
    }

    @Transactional
    fun update(id: UUID, genreUpdateRequestDto: GenreUpdateRequestDto): GenreResponseDto {
        val genreForUpdate = this.genreRepository.findById(id) ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH GENRE $id"
        )

        val validator: Validator = IsDeletedValidator(genreForUpdate.isDeleted, Genre.DOMAIN)
        validator.validate()

        genreForUpdate.update(genreUpdateRequestDto.name)

        return GenreResponseDto.from(
            this.genreRepository.update(
                id,
                genreForUpdate
            ) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH GENRE $id"
            )
        )
    }
}
