package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object GenreTable : UUIDTable(name = "TB_GENRE") {
    val name: Column<String> = varchar("name", 255)
}

class Genre(var id: UUID, var name: String) {
    companion object {
        fun from(genreEntity: GenreEntity) = Genre(
            id = genreEntity.id.value,
            name = genreEntity.name
        )
    }
}

class GenreEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<GenreEntity>(GenreTable)

    var name by GenreTable.name
}
