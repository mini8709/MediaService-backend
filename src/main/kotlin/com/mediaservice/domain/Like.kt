package com.mediaservice.domain

import org.jetbrains.exposed.sql.Table

object LikeTable : Table(name = "TB_LIKE") {
    val profile = reference("profile", ProfileTable)
    val mediaContents = reference("mediaAllSeries", MediaContentsTable)
    override val primaryKey = PrimaryKey(this.profile, this.mediaContents, name = "PK_Like_pf_mg")
}

class Like(
    var profile: Profile,
    var mediaContents: MediaContents
) {
    companion object {
        fun of(profile: Profile, mediaContents: MediaContents) = Like(
            profile = profile,
            mediaContents = mediaContents
        )
    }
}
