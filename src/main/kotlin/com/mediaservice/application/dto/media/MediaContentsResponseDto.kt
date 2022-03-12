package com.mediaservice.application.dto.media

import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaContents
import com.mediaservice.domain.MediaSeries
import java.util.UUID

data class MediaContentsResponseDto(
    val id: UUID,
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean,
    val mediaSeriesList: List<MediaSeriesResponseDto>,
    val mediaList: List<MediaResponseDto>,
    val isLike: Boolean,
    val genreList: List<GenreResponseDto>?,
    val actorList: List<ActorResponseDto>?,
    val creatorList: List<CreatorResponseDto>?
) {
    companion object {
        fun from(
            mediaContents: MediaContents,
            mediaSeriesList: List<MediaSeries>,
            mediaList: List<Media>,
            isLike: Boolean
        ) = MediaContentsResponseDto(
            id = mediaContents.id!!,
            title = mediaContents.title,
            synopsis = mediaContents.synopsis,
            trailer = mediaContents.trailer,
            thumbnail = mediaContents.thumbnail,
            rate = mediaContents.rate,
            isSeries = mediaContents.isSeries,
            mediaSeriesList = mediaSeriesList.map { MediaSeriesResponseDto.from(it) },
            mediaList = mediaList.map { MediaResponseDto.from(it) },
            isLike = isLike,
            genreList = mediaContents.genreList!!.map { GenreResponseDto.from(it) },
            actorList = mediaContents.actorList!!.map { ActorResponseDto.from(it) },
            creatorList = mediaContents.creatorList!!.map { CreatorResponseDto.from(it) }
        )
    }
}
