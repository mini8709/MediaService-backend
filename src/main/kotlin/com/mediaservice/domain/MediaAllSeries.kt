package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object MediaAllSeriesTable : UUIDTable(name = "TB_MEDIAALLSERIES") {
    val title: Column<String> = varchar("title", 255)
    val synopsis: Column<String> = text("synopsis")
    val trailer: Column<String> = varchar("trailer", 255)
    val thumbnail: Column<String> = varchar("thumbnail", 255)
    val rate: Column<String> = varchar("age_rate", 255)
    val isSeries: Column<Boolean> = bool("is_series")
}

class MediaAllSeries(
    var id: UUID,
    var title: String,
    var synopsis: String,
    var trailer: String,
    var thumbnail: String,
    var rate: String,
    var isSeries: Boolean
) {
    companion object {
        fun from(mediaAllSeriesEntity: MediaAllSeriesEntity) = MediaAllSeries(
            id = mediaAllSeriesEntity.id.value,
            title = mediaAllSeriesEntity.title,
            synopsis = mediaAllSeriesEntity.synopsis,
            trailer = mediaAllSeriesEntity.trailer,
            thumbnail = mediaAllSeriesEntity.thumbnail,
            rate = mediaAllSeriesEntity.rate,
            isSeries = mediaAllSeriesEntity.isSeries
        )
    }
}

class MediaAllSeriesEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MediaAllSeriesEntity>(MediaAllSeriesTable)

    var title by MediaAllSeriesTable.title
    var synopsis by MediaAllSeriesTable.synopsis
    var trailer by MediaAllSeriesTable.trailer
    var thumbnail by MediaAllSeriesTable.thumbnail
    var rate by MediaAllSeriesTable.rate
    var isSeries by MediaAllSeriesTable.isSeries
    val mediaGroupInfoList by MediaSeriesEntity referrersOn MediaSeriesTable.mediaAllSeries
    var actorList by ActorEntity via MediaAllSeriesActorTable
    var creatorList by CreatorEntity via MediaAllSeriesCreatorTable
    var genreList by GenreEntity via MediaAllSeriesGenreTable
}

object MediaAllSeriesActorTable : Table() {
    val mediaAllSeries = reference("media_group", MediaAllSeriesTable)
    val actor = reference("actor", ActorTable)
    override val primaryKey = PrimaryKey(this.mediaAllSeries, this.actor, name = "PK_MediaAllSeriesActor_mg_act")
}

object MediaAllSeriesCreatorTable : Table() {
    val mediaAllSeries = reference("media_group", MediaAllSeriesTable)
    val creator = reference("creator", CreatorTable)
    override val primaryKey = PrimaryKey(this.mediaAllSeries, this.creator, name = "PK_MediaAllSeriesDirector_mg_crt")
}

object MediaAllSeriesGenreTable : Table() {
    val mediaAllSeries = reference("media_group", MediaAllSeriesTable)
    val genre = reference("genre", GenreTable)
    override val primaryKey = PrimaryKey(this.mediaAllSeries, this.genre, name = "PK_MediaAllSeriesGenre_mg_gen")
}
