package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object MediaTable : UUIDTable(name = "TB_MEDIA") {
    val name: Column<String> = varchar("name", 255)
    val synopsis: Column<String> = text("synopsis")
    val order: Column<Int> = integer("order")
    val mediaSeries = reference("media_series", MediaSeriesTable)
}

class Media(
    var id: UUID,
    var name: String,
    var synopsis: String,
    var order: Int,
    var mediaSeries: MediaSeries
) {
    companion object {
        fun from(mediaEntity: MediaEntity) = Media(
            id = mediaEntity.id.value,
            name = mediaEntity.name,
            synopsis = mediaEntity.synopsis,
            order = mediaEntity.order,
            mediaSeries = MediaSeries.from(mediaEntity.mediaSeries)
        )
    }
}

class MediaEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MediaEntity>(MediaTable)

    var name by MediaTable.name
    var synopsis by MediaTable.synopsis
    var order by MediaTable.order
    var mediaSeries by MediaSeriesEntity referencedOn MediaTable.mediaSeries
}
