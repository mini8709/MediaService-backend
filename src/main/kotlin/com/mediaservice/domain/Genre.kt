package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object GenreTable : UUIDTable(name = "TB_GENRE") {
    val name: Column<String> = varchar("name", 255)
    val isDeleted: Column<Boolean> = bool("isDeleted")
}

class Genre(var id: UUID?, var name: String, var isDeleted: Boolean) {
    companion object {
        const val DOMAIN = "GENRE"

        fun from(genreEntity: GenreEntity) = Genre(
            id = genreEntity.id.value,
            name = genreEntity.name,
            isDeleted = genreEntity.isDeleted
        )

        fun of(name: String) = Genre(
            id = null,
            name = name,
            isDeleted = false
        )
    }
}

class GenreEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<GenreEntity>(GenreTable)

    var name by GenreTable.name
    var isDeleted by GenreTable.isDeleted
}
