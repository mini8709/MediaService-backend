package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object ActorTable : UUIDTable(name = "TB_ACTOR") {
    val name: Column<String> = varchar("name", 255)
}

class Actor(var id: UUID, var name: String) {
    companion object {
        fun from(actorEntity: ActorEntity) = Actor(
            id = actorEntity.id.value,
            name = actorEntity.name
        )
    }
}

class ActorEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ActorEntity>(ActorTable)

    var name by ActorTable.name
}
