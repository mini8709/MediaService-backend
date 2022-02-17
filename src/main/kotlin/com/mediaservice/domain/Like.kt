package com.mediaservice.domain

import org.jetbrains.exposed.sql.Table

object LikeTable : Table(name = "TB_LIKE") {
    val profile = reference("profile", ProfileTable)
    val mediaAllSeries = reference("mediaAllSeries", MediaAllSeriesTable)
    override val primaryKey = PrimaryKey(this.profile, this.mediaAllSeries, name = "PK_Like_pf_mg")
}

class Like(
    var profile: Profile,
    var mediaAllSeries: MediaAllSeries
) {
    companion object {
        fun of(profile: Profile, mediaAllSeries: MediaAllSeries) = Like(
            profile = profile,
            mediaAllSeries = mediaAllSeries
        )
    }
}
