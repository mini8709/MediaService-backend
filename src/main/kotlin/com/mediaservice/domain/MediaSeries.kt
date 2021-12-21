package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object MediaSeriesTable : UUIDTable(name = "TB_MEDIASERIES") {
    val title: Column<String> = varchar("title", 255)
    val order: Column<Int> = integer("order")
    val mediaAllSeries = reference("media_all_series", MediaAllSeriesTable)
}

class MediaSeries(var id: UUID, var title: String, var order: Int, var mediaAllSeries: MediaAllSeries) {
    companion object {
        fun from(mediaSeriesEntity: MediaSeriesEntity) = MediaSeries(
            id = mediaSeriesEntity.id.value,
            title = mediaSeriesEntity.title,
            order = mediaSeriesEntity.order,
            mediaAllSeries = MediaAllSeries.from(mediaSeriesEntity.mediaAllSeries)
        )
    }
}

class MediaSeriesEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MediaSeriesEntity>(MediaSeriesTable)

    var title by MediaSeriesTable.title
    var order by MediaSeriesTable.order
    var mediaAllSeries by MediaAllSeriesEntity referencedOn MediaSeriesTable.mediaAllSeries
    val mediaList by MediaEntity referrersOn MediaTable.mediaSeries
}
