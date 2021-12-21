package com.mediaservice.application.dto

import com.mediaservice.domain.Actor
import java.util.UUID

data class ActorResponseDto(
    val id: UUID,
    val name: String
) {
    companion object {
        fun from(actor: Actor) = ActorResponseDto(
            id = actor.id!!,
            name = actor.name
        )
    }
}
