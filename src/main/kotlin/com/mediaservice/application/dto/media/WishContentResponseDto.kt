package com.mediaservice.application.dto.media

import com.mediaservice.domain.WishContent
import java.util.UUID

data class WishContentResponseDto(
    val id: UUID,
    val profileName: String,
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean

) {
    companion object {
        fun from(wishContent: WishContent) = WishContentResponseDto(
            id = wishContent.id!!,
            profileName = wishContent.profile.name,
            title = wishContent.mediaContents.title,
            synopsis = wishContent.mediaContents.synopsis,
            trailer = wishContent.mediaContents.trailer,
            thumbnail = wishContent.mediaContents.thumbnail,
            rate = wishContent.mediaContents.rate,
            isSeries = wishContent.mediaContents.isSeries
        )
    }
}
