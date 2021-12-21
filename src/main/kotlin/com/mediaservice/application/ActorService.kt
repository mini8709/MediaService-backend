package com.mediaservice.application

import com.mediaservice.application.dto.ActorCreateRequestDto
import com.mediaservice.application.dto.ActorResponseDto
import com.mediaservice.domain.Actor
import com.mediaservice.domain.repository.ActorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActorService(
    private val actorRepository: ActorRepository
) {
    @Transactional
    fun create(
        actorCreateRequestDto: ActorCreateRequestDto
    ): ActorResponseDto {
        return ActorResponseDto.from(
            this.actorRepository.save(
                Actor.of(
                    actorCreateRequestDto.name
                )
            )
        )
    }
}
