package com.mediaservice.application

import com.mediaservice.application.dto.media.CreatorCreateRequestDto
import com.mediaservice.application.dto.media.CreatorResponseDto
import com.mediaservice.application.dto.media.CreatorUpdateRequestDto
import com.mediaservice.domain.Creator
import com.mediaservice.domain.repository.CreatorRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreatorService(
    private val creatorRepository: CreatorRepository
) {
    @Transactional
    fun create(creatorCreateRequestDto: CreatorCreateRequestDto): CreatorResponseDto {
        return CreatorResponseDto.from(
            this.creatorRepository.save(
                Creator.of(creatorCreateRequestDto.name)
            )
        )
    }
    @Transactional
    fun update(id: UUID, creatorCreateRequestDto: CreatorUpdateRequestDto): CreatorResponseDto {
        val creatorForUpdate = this.creatorRepository.findById(id) ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH CREATOR $id"
        )

        if (!creatorForUpdate.isDeleted) {
            creatorForUpdate.name = creatorCreateRequestDto.name
        } else throw BadRequestException(ErrorCode.ROW_ALREADY_DELETED, "DELETED CREATOR")

        return CreatorResponseDto.from(
            this.creatorRepository.update(
                id,
                creatorForUpdate
            ) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH CREATOR $id"
            )
        )
    }
}
