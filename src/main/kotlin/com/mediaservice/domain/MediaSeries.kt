package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object MediaSeriesTable : UUIDTable(name = "TB_MEDIA_SERIES") {
    val title: Column<String> = varchar("title", 255)
    val order: Column<Int> = integer("order")
    val isDeleted: Column<Boolean> = bool("isDeleted")
    val mediaContents = reference("media_contents", MediaContentsTable)
}

class MediaSeries(
    var id: UUID?,
    var title: String,
    var order: Int,
    var isDeleted: Boolean,
    var mediaContents: MediaContents
) {
    companion object {
        fun from(mediaSeriesEntity: MediaSeriesEntity) = MediaSeries(
            id = mediaSeriesEntity.id.value,
            title = mediaSeriesEntity.title,
            order = mediaSeriesEntity.order,
            isDeleted = mediaSeriesEntity.isDeleted,
            mediaContents = MediaContents.from(mediaSeriesEntity.mediaContents)
        )

        fun of(title: String, order: Int, mediaContents: MediaContents) = MediaSeries(
            id = null,
            title = title,
            order = order,
            isDeleted = false,
            mediaContents = mediaContents
        )
    }
}

class MediaSeriesEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MediaSeriesEntity>(MediaSeriesTable)

    var title by MediaSeriesTable.title
    var order by MediaSeriesTable.order
    var isDeleted by MediaSeriesTable.isDeleted
    var mediaContents by MediaContentsEntity referencedOn MediaSeriesTable.mediaContents
}
