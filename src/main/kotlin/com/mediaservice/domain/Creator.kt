package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object CreatorTable : UUIDTable(name = "TB_CREATOR") {
    val name: Column<String> = varchar("name", 255)
}

class Creator(var id: UUID, var name: String) {
    companion object {
        fun from(creatorEntity: CreatorEntity) = Creator(
            id = creatorEntity.id.value,
            name = creatorEntity.name
        )
    }
}

class CreatorEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CreatorEntity>(CreatorTable)

    var name by CreatorTable.name
}
