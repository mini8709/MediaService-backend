package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object ActorTable : UUIDTable(name = "TB_ACTOR") {
    val name: Column<String> = varchar("name", 255)
    val isDeleted: Column<Boolean> = bool("isDeleted")
}

class Actor(var id: UUID?, var name: String, var isDeleted: Boolean) {
    companion object {
        const val DOMAIN: String = "ACTOR"

        fun from(actorEntity: ActorEntity) = Actor(
            id = actorEntity.id.value,
            name = actorEntity.name,
            isDeleted = actorEntity.isDeleted
        )

        fun of(name: String) = Actor(
            id = null,
            name = name,
            isDeleted = false
        )
    }
}

class ActorEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ActorEntity>(ActorTable)

    var name by ActorTable.name
    var isDeleted by ActorTable.isDeleted
}
