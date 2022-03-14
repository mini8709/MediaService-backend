package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object WishContentTable : UUIDTable("TB_WISHCONTENT") {
    val profile = reference("profile", ProfileTable)
    val mediaContents = reference("mediaContents", MediaContentsTable)
    val isDeleted: Column<Boolean> = bool("isDeleted")
}

class WishContent(
    var id: UUID?,
    var mediaContents: MediaContents,
    var profile: Profile,
    var isDeleted: Boolean
) {
    companion object {
        fun from(wishContentEntity: WishContentEntity) = WishContent(
            id = wishContentEntity.id.value,
            mediaContents = MediaContents.from(wishContentEntity.mediaContents),
            profile = Profile.from(wishContentEntity.profile),
            isDeleted = false
        )
        fun of(mediaContents: MediaContents, profile: Profile) = WishContent(
            id = null,
            mediaContents = mediaContents,
            profile = profile,
            isDeleted = false
        )
    }
}

class WishContentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<WishContentEntity>(WishContentTable)

    var mediaContents by MediaContentsEntity referencedOn WishContentTable.mediaContents
    var profile by ProfileEntity referencedOn WishContentTable.profile
    var isDeleted by WishContentTable.isDeleted
}
