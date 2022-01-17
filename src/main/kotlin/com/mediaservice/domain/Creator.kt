package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object CreatorTable : UUIDTable(name = "TB_CREATOR") {
    val name: Column<String> = varchar("name", 255)
    val isDeleted: Column<Boolean> = bool("isDeleted")
}

class Creator(var id: UUID?, var name: String, var isDeleted: Boolean) {
    companion object {
        const val DOMAIN = "CREATOR"

        fun from(creatorEntity: CreatorEntity) = Creator(
            id = creatorEntity.id.value,
            name = creatorEntity.name,
            isDeleted = creatorEntity.isDeleted
        )

        fun of(name: String) = Creator(
            id = null,
            name = name,
            isDeleted = false
        )
    }
}

class CreatorEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CreatorEntity>(CreatorTable)

    var name by CreatorTable.name
    var isDeleted by CreatorTable.isDeleted
}
