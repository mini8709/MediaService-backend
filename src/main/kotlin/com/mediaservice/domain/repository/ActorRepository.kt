package com.mediaservice.domain.repository

import com.mediaservice.domain.Actor
import com.mediaservice.domain.ActorEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ActorRepository {
    fun save(actor: Actor): Actor {
        return ActorEntity.new {
            name = actor.name
        }.let { Actor.from(it) }
    }

    fun findById(id: UUID): Actor? {
        return ActorEntity.findById(id)?.let {
            Actor.from(it)
        }
    }
}
