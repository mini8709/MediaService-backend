package com.mediaservice.application

import com.mediaservice.application.dto.CreatorCreateRequestDto
import com.mediaservice.application.dto.CreatorResponseDto
import com.mediaservice.domain.Creator
import com.mediaservice.domain.repository.CreatorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatorService(
    private val creatorRepository: CreatorRepository
) {
    @Transactional
    fun create(
        creatorCreateRequestDto: CreatorCreateRequestDto
    ): CreatorResponseDto {
        return CreatorResponseDto.from(
            this.creatorRepository.save(
                Creator.of(
                    creatorCreateRequestDto.name
                )
            )
        )
    }
}
